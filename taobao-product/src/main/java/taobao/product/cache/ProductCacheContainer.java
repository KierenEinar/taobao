package taobao.product.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taobao.product.vo.ProductDetailVo;
import java.util.Objects;


@Component
public class ProductCacheContainer implements DisposableBean {

    @Autowired
    Cache<Long, ProductDetailVo> caffeineCache;

    Logger logger = LoggerFactory.getLogger(ProductCacheContainer.class);

    public void putProductCacheByHash (Long productId,  ProductDetailVo cache) {
        logger.info("productId -> {}, cache -> {}", productId, cache);
        caffeineCache.put(productId, cache);
    }

    public ProductDetailVo getProductCache (Long productId) {
        return caffeineCache.getIfPresent(productId);
    }


    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(caffeineCache)) caffeineCache.cleanUp();
        logger.info("ProductCacheContainer destroy success ... ");
    }
}
