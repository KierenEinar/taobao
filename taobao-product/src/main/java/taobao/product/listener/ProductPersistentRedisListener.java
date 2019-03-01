package taobao.product.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.product.constant.Constant;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

import java.util.Objects;

@RocketMQMessageListener(consumerGroup = Constant.Producer.product_create_redis_group, topic = Constant.Topic.product_create_redis_topic)
@Service
public class ProductPersistentRedisListener implements RocketMQListener<Long> {

    @Autowired
    ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductPersistentRedisListener.class);

    @Override
    public void onMessage(Long productId) {
        logger.info("ProductPersistentRedisListener, productId -> {}", productId);
        ProductDetailVo productDetailVo = productService.findProductDetailFromDB(productId);
        if (Objects.nonNull(productDetailVo)) {
            productService.createProduct2Redis(productId, productDetailVo);
        } else {
            productService.putNotExistsProductByBloomFilter(productId);
        }
    }
}
