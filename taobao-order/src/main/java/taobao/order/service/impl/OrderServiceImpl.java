package taobao.order.service.impl;

import com.google.common.collect.Lists;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryLockIncrVo;
import taobao.core.vo.InventoryWebVo;
import taobao.core.vo.OrderPayVo;
import taobao.order.dto.OrderItemDto;
import taobao.order.dto.ProductSpecesDto;
import taobao.order.exception.OrderException;
import taobao.order.mapper.OrderDetailMapper;
import taobao.order.mapper.OrderMapper;
import taobao.order.model.Order;
import taobao.order.model.OrderDetail;
import taobao.order.producer.ProducerService;
import taobao.order.service.InventoryService;
import taobao.order.service.OrderService;
import taobao.order.service.ProductService;
import java.util.Map;

import taobao.order.vo.OrderPayWebVo;
import taobao.order.vo.OrderWebVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper ordersMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    ProducerService producerService;

    @Override
    @Transactional
    public Boolean createOrder(OrderWebVo orderWebVo) {

        //获取商品信息

        APIResponse<List<ProductSpecesDto>> speces = productService.findSpeces(orderWebVo.getDetails());

        if (speces.getCode()!=200) throw new IllegalArgumentException("非法请求, 无法找到商品");

        //检查重复提交

        //锁库存
        APIResponse<Boolean> apiResponse = inventoryService.batchPreIncrInventory(orderWebVo.getDetails());

        if (Boolean.FALSE.equals(apiResponse.getData())) return Boolean.FALSE;

        //生成订单
        OrderItemDto orderItemDto = buildOrder(orderWebVo, speces.getData());
        Order orders = orderItemDto.getOrder();

        try{
            int result = ordersMapper.insert(orders);
            if (result == 0) {
                throw new OrderException("生成订单失败");
            }

            //生成订单项
            List<OrderDetail> details = orderItemDto.getDetails();
            result = orderDetailMapper.insertBatch (details);
            if (result == 0)  {
                throw new OrderException("生成订单失败");
            }
        }catch (Exception e) {
            producerService.sendProductStockBackMessage(orderWebVo.getDetails());
            throw e;
        }

        //等待付款, 生成延时消息队列(如果没有在规定时间内付款则恢复mysql库存)

        producerService.sendProductStockUnLockWhileTimeout(orders);

        return Boolean.TRUE;
    }

    @Override
    public Boolean updateOrderStatus(Long id, String status, String preStatus, Long userId) {
        return ordersMapper.updateStatusByPreStatusAndId(id, null, status, preStatus, new Date(), userId) > 0;
    }

    @Override
    public String findOrderStatus(Long id, Long userId) {
        return ordersMapper.selectByIdAndUserId(id, userId).getStatus();
    }

    @Override
    public List<OrderDetail> findDetails(Long id, Long userId) {
        return orderDetailMapper.selectByOrderIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public Boolean payOrder(OrderPayVo orderPayVo) {

        //幂等性校验
        OrderPayWebVo orderPayWebVo = new OrderPayWebVo();
        orderPayWebVo.setOrderId(Long.parseLong(orderPayVo.getOrderId()));
        orderPayWebVo.setUserId(orderPayVo.getUserId());

        Order order = ordersMapper.selectByPrimaryKey(orderPayWebVo.getOrderId());

        try{
            if (Objects.isNull(order)) throw new OrderException("无法找到当前订单");
            //订单状态修改为已支付
            int result = ordersMapper.updateStatusByPreStatusAndId(order.getId(), orderPayVo.getTradeNo() + "", Order.Status.paied, Order.Status.unpaying, new Date(), orderPayWebVo.getUserId());
            if (result == 0) throw new OrderException("订单状态修改失败");
        }catch (Exception e) {
            //订单状态修改失败, 发生一笔退款
            producerService.sendOrderPayFailed(orderPayVo);
            return Boolean.FALSE;
        }

        producerService.sendInventoryLcckIncrMessage(findInventorysLockByOrder(order));

        return Boolean.TRUE;
    }

    private List<InventoryWebVo> findInventorysByOrder(Order order) {

        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderIdAndUserId(order.getId(), order.getUserId());
        return orderDetails.stream().map(i->{
            InventoryWebVo inventoryWebVo = new InventoryWebVo();
            inventoryWebVo.setNums(0 - i.getQuantity());
            inventoryWebVo.setProductId(i.getProductId());
            inventoryWebVo.setSpecsId(i.getProductSpecsId());
            return inventoryWebVo;
        }).collect(Collectors.toList());
    }


    private List<InventoryLockIncrVo> findInventorysLockByOrder(Order order) {

        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderIdAndUserId(order.getId(), order.getUserId());
        return orderDetails.stream().map(i->{
            InventoryLockIncrVo inventoryLockIncrVo = new InventoryLockIncrVo();
            inventoryLockIncrVo.setCreateTime(new Date());
            inventoryLockIncrVo.setId(keyGenerator.generateKey().longValue());
            inventoryLockIncrVo.setIncrLockNum(0 - i.getQuantity());
            inventoryLockIncrVo.setProductId(i.getProductId());
            inventoryLockIncrVo.setSpecsId(i.getProductSpecsId());
            return inventoryLockIncrVo;
        }).collect(Collectors.toList());
    }


    private OrderItemDto buildOrder (OrderWebVo orderWebVo, List<ProductSpecesDto> data) {
        OrderItemDto orderItemDto = new OrderItemDto();
        Order order = new Order();
        order.setUserId(orderWebVo.getUserId());
        order.setCreateTime(new Date());
        order.setId(keyGenerator.generateKey().longValue());
        BigDecimal total = new BigDecimal(0.00);
        order.setStatus(Order.Status.unpaying);
        Map<Long, ProductSpecesDto> priceMap = data.stream().collect(Collectors.toMap(ProductSpecesDto::getId, v->v));
        List<InventoryWebVo> details = orderWebVo.getDetails();
        List<OrderDetail> orderDetails = Lists.newArrayList();
        for (InventoryWebVo d : details) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCreateTime(new Date());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(d.getProductId());
            BigDecimal price = priceMap.get(d.getSpecsId()).getPrice();
            price.setScale(2, BigDecimal.ROUND_FLOOR);
            orderDetail.setPrice(price);
            orderDetail.setQuantity(d.getNums());
            orderDetail.setUserId(orderWebVo.getUserId());
            orderDetail.setProductSpecsId(d.getSpecsId());
            total = total.add(orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getQuantity())));
            orderDetails.add(orderDetail);
        }
        total.setScale(2, BigDecimal.ROUND_FLOOR);
        order.setTotalCost(total);
        orderItemDto.setOrder(order);
        orderItemDto.setDetails(orderDetails);
        return orderItemDto;
    }


}
