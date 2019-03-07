package taobao.order.listener;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import taobao.core.constant.Constant;
import taobao.order.model.Order;
import taobao.order.service.OrderService;
import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionState;

@RocketMQTransactionListener(producerGroup = Constant.TransactionProducer.product_order_timeout_group)
public class LocalOrderTransactionExecutor implements RocketMQLocalTransactionListener {

    @Autowired
    OrderService orderService;

    Logger logger = LoggerFactory.getLogger(LocalOrderTransactionExecutor.class);

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object obj) {
        Order order = parseOrderByMessage(message);
        logger.info("executeLocalTransaction, begin execute order timeout update .. , orderId ->{}", order.getId());
        Boolean result = orderService.updateOrderStatus(order.getId(), Order.Status.timeout, Order.Status.unpaying, order.getUserId());
        logger.info("executeLocalTransaction, end execute order timeout update .. , orderId ->{}, result -> {}", order.getId(), result);
        if (Boolean.TRUE.equals(result)) return RocketMQLocalTransactionState.COMMIT;
        else if (Boolean.FALSE.equals(result)) return RocketMQLocalTransactionState.ROLLBACK;
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        Order order = parseOrderByMessage(message);
        String status = orderService.findOrderStatus(order.getId(), order.getUserId());
        logger.info("checkLocalTransaction, status->{} ,orderId ->{}", status, order.getId());
        if (Order.Status.timeout.equals(status)) return RocketMQLocalTransactionState.COMMIT;
        if (Order.Status.unpaying.equals(status)) return RocketMQLocalTransactionState.UNKNOWN;
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    private Order parseOrderByMessage (Message message) {
        byte bytes[] = message.getBody();
        String body = new String(bytes);
        return JSON.parseObject(body, Order.class);
    }

}
