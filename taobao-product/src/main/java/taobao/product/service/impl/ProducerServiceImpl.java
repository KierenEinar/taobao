package taobao.product.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.vo.InventoryWebVo;
import taobao.core.constant.Constant;
import taobao.product.service.ProducerService;
import taobao.rocketmq.core.RocketMQTemplate;

import java.util.List;

@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private class SendCallbackImpl<T> implements SendCallback{

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
    public void sendCreateProdcut2CacheMessage(Long productId) {
        rocketMQTemplate.sendAsync(Constant.Topic.product_create_redis_topic, productId, new SendCallbackImpl(productId));
    }

    @Override
    public void sendCreateProductMQInTransaction(Long productId) {
        rocketMQTemplate.sendMessageInTransaction(Constant.TransactionProducer.product_release_group, Constant.Topic.product_create_redis_transaction_topic, productId, null);
    }

    @Override
    public void sendDelCacheMsgDelay(Long productId) {
        rocketMQTemplate.sendAsync(Constant.Topic.product_detail_cache_del_topic, productId, new SendCallbackImpl(productId), Constant.Timeout.product_detail_cache_timeout, 2);
    }

    @Override
    public void sendProductStockBackMessage(List<InventoryWebVo> inventoryWebVoList) {
        inventoryWebVoList.forEach(i->{
            rocketMQTemplate.sendAsync(Constant.Topic.inventory_back_topic, i, new SendCallbackImpl(i));
        });
    }
}
