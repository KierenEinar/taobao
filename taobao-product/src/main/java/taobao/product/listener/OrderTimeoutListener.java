package taobao.product.listener;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import taobao.core.RedisService;
import taobao.core.constant.Constant;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;
import taobao.product.constant.RedisPrefix;
import taobao.product.dto.Order;
import taobao.product.dto.OrderDetail;
import taobao.product.service.InventoryService;
import taobao.product.service.OrderService;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.order_transaction_group, topic = Constant.Topic.order_timeout_topic)
public class OrderTimeoutListener implements RocketMQListener<String> {

    @Autowired
    OrderService orderService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    RedisService redisService;

    Logger logger = LoggerFactory.getLogger(OrderTimeoutListener.class);

    @Override
    public void onMessage(String json) {

        Order order = JSON.parseObject(json, Order.class);

        Boolean result = redisService.setNX(RedisPrefix.productInventoryIdempotency(order.getId()), new Date().toString());
        if (Boolean.FALSE.equals(result)) {
            logger.info("实现了幂等性, orderId -> {}", order.getId());
            return;
        }
        APIResponse<List<OrderDetail>> apiResponse = orderService.findDetailsByOrderIdAndUserId(order.getId(), order.getUserId());
        if (apiResponse.getCode()!=200) throw new RuntimeException();
        List<OrderDetail> detailList = apiResponse.getData();
        List<InventoryWebVo> inventoryWebVos = detailList.stream().map(i->{
            InventoryWebVo inventoryWebVo = new InventoryWebVo();
            inventoryWebVo.setNums(0-i.getQuantity());
            inventoryWebVo.setProductId(i.getProductId());
            inventoryWebVo.setSpecsId(i.getProductSpecsId());
            return inventoryWebVo;
        }).collect(Collectors.toList());

        logger.info("回扣库存, ->{}", inventoryWebVos);

        if (CollectionUtils.isEmpty(inventoryWebVos)) return;
        inventoryService.batchPreIncrInventory(inventoryWebVos);
    }

}
