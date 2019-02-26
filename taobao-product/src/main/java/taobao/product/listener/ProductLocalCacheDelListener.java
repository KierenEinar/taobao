package taobao.product.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.product.cache.ProductCacheContainer;
import taobao.product.constant.Constant;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;
@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.product_detail_cache_group, topic = Constant.Topic.product_detail_cache_del_topic)
public class ProductLocalCacheDelListener implements RocketMQListener<Long> {

    Logger logger = LoggerFactory.getLogger(ProductLocalCacheDelListener.class);

    @Autowired
    ProductCacheContainer productCacheContainer;

    @Override
    public void onMessage(Long productId) {
        productCacheContainer.clearProductCache(productId);
        logger.info("delete product detail in local cache success -> {}", productId);
    }
}
