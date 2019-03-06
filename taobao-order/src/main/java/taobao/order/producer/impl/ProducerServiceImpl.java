package taobao.order.producer.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.constant.Constant;
import taobao.core.vo.InventoryWebVo;
import taobao.order.producer.ProducerService;
import taobao.rocketmq.core.RocketMQTemplate;

import java.util.List;

@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private class SendCallbackImpl<T> implements SendCallback {

        private T data;

        public SendCallbackImpl (T data) {
            this.data = data;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            logger.info("send message success, data -> {}", data);
        }

        @Override
        public void onException(Throwable e) {
            logger.error("send message error, data -> {}, err -> {}", data, e.getMessage());
        }
    }

    @Override
    public void sendProductStockBackMessage(List<InventoryWebVo> inventoryWebVoList) {
        inventoryWebVoList.forEach(i->{
            rocketMQTemplate.sendAsync(Constant.Topic.inventory_back_topic, i, new SendCallbackImpl(i));
        });
    }

    @Override
    public void sendProductStockUnLockWhileTimeout(Long id, List<InventoryWebVo> details) {
        rocketMQTemplate.sendAsync(Constant.Topic.order_timeout_topic, id, new SendCallbackImpl<>(id));
        sendProductStockBackMessage(details);
    }
}
