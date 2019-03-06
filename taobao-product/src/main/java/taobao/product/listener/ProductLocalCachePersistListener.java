package taobao.product.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import taobao.localmq.annotation.LocalMQMessageListener;
import taobao.localmq.core.LocalMQListener;
import taobao.product.cache.ProductCacheContainer;
import taobao.core.constant.Constant;
import taobao.product.vo.ProductDetailVo;


@LocalMQMessageListener(topic = Constant.Topic.product_create_local_cache_topic)
public class ProductLocalCachePersistListener implements LocalMQListener<ProductDetailVo> {

    Logger logger = LoggerFactory.getLogger(ProductLocalCachePersistListener.class);

    @Autowired
    ProductCacheContainer productCacheContainer;

    @Override
    public void onMessage(ProductDetailVo productDetailVo) {
        logger.info("ProductLocalCachePersistListener, on message -> {}", productDetailVo);
        productCacheContainer.putProductCacheByHash(productDetailVo.getProduct().getProductId(), productDetailVo);
    }
}
