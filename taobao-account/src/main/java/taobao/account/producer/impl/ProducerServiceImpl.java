package taobao.account.producer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.account.producer.ProducerService;
import taobao.core.constant.Constant;
import taobao.core.vo.OrderPayVo;
import taobao.rocketmq.core.RocketMQTemplate;

@Service
public class ProducerServiceImpl implements ProducerService {

   @Autowired
    RocketMQTemplate rocketMQTemplate;

    Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    @Override
    public void notifyPaySuccess(OrderPayVo orderPayVo) {

        String json = JSON.toJSONString(orderPayVo);

        rocketMQTemplate.sendAsync(Constant.Topic.pay_success_notify_order, json, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("send notifyPaySuccess ... orderId -> {}", json);
            }
            @Override
            public void onException(Throwable e) {
                logger.error("send notifyPaySuccess ...error -> {}", e.getMessage());
            }
        });
    }
}
