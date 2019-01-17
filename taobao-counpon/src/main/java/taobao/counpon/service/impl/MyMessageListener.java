package taobao.counpon.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import taobao.counpon.model.UsersCounpon;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@Component
@RocketMQMessageListener(consumerGroup= "taobao-counpon-consumer",topic = "myTopic")
public class MyMessageListener implements RocketMQListener<String> {

    Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onMessage(String message) {
        logger.info("收到mq -> {}", message);
    }
}
