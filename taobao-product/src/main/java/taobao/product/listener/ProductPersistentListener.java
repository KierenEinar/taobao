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

@Service
@RocketMQMessageListener(consumerGroup=Constant.Producer.product_create_redis_transaction_group, topic = Constant.Topic.product_create_redis_transaction_topic)
public class ProductRedisListener implements RocketMQListener<Long> {

    @Autowired
    ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductRedisListener.class);

    @Override
    public void onMessage(Long productId) {
        logger.info("consume message, topic -> {}, body -> {}", Constant.Topic.product_create_topic, productId);
        ProductDetailVo productDetailVo = productService.findProductDetailFromDB(productId);
        productService.releaseProductStock2Redis(productId);
        productService.createProduct2Hbase(productId, productDetailVo);
        productService.createProduct2Redis(productId, productDetailVo);
    }


}
