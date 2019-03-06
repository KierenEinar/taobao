package taobao.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.core.model.APIResponse;
import taobao.order.mapper.OrdersMapper;
import taobao.order.model.Orders;
import taobao.order.service.InventoryService;
import taobao.order.service.OrderService;
import taobao.order.service.ProductService;
import taobao.order.vo.OrderWebVo;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    @Override
    @Transactional
    public Boolean createOrder(OrderWebVo orderWebVo) {

        //获取商品信息

         //productService.findSpeces(orderWebVo.getDetails());


        //检查重复提交

        //锁库存

        APIResponse<Boolean> apiResponse = inventoryService.batchLockInventorys(orderWebVo.getDetails());

        if (Boolean.FALSE.equals(apiResponse.getData())) return Boolean.FALSE;


        //生成订单
        //ordersMapper.insert(Order);

        //等待付款, 生成延时消息队列(如果没有在规定时间内付款则恢复redis库存)


        return Boolean.TRUE;
    }




    private Orders createOrder (OrderWebVo orderWebVo) {
        Orders orders = new Orders();
        orders.setUserId(orderWebVo.getUserId());
        orders.setCreateTime(new Date());

        BigDecimal total = new BigDecimal(0.00);

//        orderWebVo.getDetails().forEach(i->{
//            total.add(i.getNums() * )
//        });

        orders.setStatus(Orders.Status.creating);
        return orders;
    }


}
