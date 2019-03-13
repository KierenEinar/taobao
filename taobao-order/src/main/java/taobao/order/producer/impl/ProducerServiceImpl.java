package taobao.order.producer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.stereotype.Service;
import taobao.core.constant.Constant;
import taobao.core.vo.InventoryWebVo;
import taobao.core.vo.OrderPayVo;
import taobao.order.model.Order;
import taobao.order.producer.ProducerService;

import taobao.order.task.DelayQueueTask;
import taobao.rocketmq.core.RocketMQTemplate;

import java.util.List;


@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    DelayQueueTask delayQueueTask;

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
    public void sendProductStockUnLockWhileTimeout(Order order) {
        delayQueueTask.put(()->{
            String message = JSONObject.toJSONString(order);
            rocketMQTemplate.sendMessageInTransaction(Constant.TransactionProducer.product_order_timeout_group, Constant.Topic.order_timeout_topic, message, null);
        },10000L);
    }

    @Override
    public void sendOrderPayFailed(OrderPayVo orderPayVo) {
        String json = JSONObject.toJSONString(orderPayVo);
        rocketMQTemplate.sendAsync(Constant.Topic.order_update_failed_topic, json, new SendCallbackImpl(json));
    }
}
