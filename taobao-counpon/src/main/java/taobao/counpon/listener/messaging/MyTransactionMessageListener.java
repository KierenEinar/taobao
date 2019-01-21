package taobao.counpon.listener.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import taobao.rocketmq.annotation.Constant;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@RocketMQMessageListener(consumerGroup = Constant.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME, topic = "tran_topic")
@Component
public class MyTransactionMessageListener implements RocketMQListener<String> {

    Logger logger = LoggerFactory.getLogger(MyTransactionMessageListener.class);

    @Override
    public void onMessage(String message) {
        logger.info("收到事务消息 -> {}", message);
    }
}
