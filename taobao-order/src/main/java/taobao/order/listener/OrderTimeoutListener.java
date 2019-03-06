package taobao.order.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import taobao.core.constant.Constant;
import taobao.order.model.Orders;
import taobao.order.service.OrderService;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@RocketMQMessageListener(consumerGroup = Constant.Producer.order_group, topic = Constant.Topic.order_timeout_topic)
public class OrderTimeoutListener implements RocketMQListener<Long> {

    @Autowired
    OrderService orderService;

    Logger logger = LoggerFactory.getLogger(OrderTimeoutListener.class);

    @Override
    public void onMessage(Long id) {
        Boolean result = orderService.updateOrderStatus(id, Orders.Status.timeout, Orders.Status.unpaying);
        if (Boolean.FALSE.equals(result)) logger.error("order timeout but update error, order_id -> {}", id);
    }
}
