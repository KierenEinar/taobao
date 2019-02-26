package taobao.product.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import taobao.product.vo.ProductDetailVo;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductCacheContainer implements InitializingBean, DisposableBean {

    public static Map<Integer, Map<Long, ProductDetailVo>> container = new ConcurrentHashMap<>();

    public static final int CONTAINER_SIZE = 100;

    Logger logger = LoggerFactory.getLogger(ProductCacheContainer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i=0; i<CONTAINER_SIZE; i++) container.put(i ,new ConcurrentHashMap<>());
        logger.info("ProductCacheContainer init success ... " );
    }

    public void putProductCacheByHash (Long productId,  ProductDetailVo cache) {
        Map<Long, ProductDetailVo> map = getProductCacheByHash(productId);
        map.put(productId, cache);
    }

    public void clearProductCache (Long productId) {
         getProductCacheByHash(productId).remove(productId);
    }


    public Integer getKeyRange (Long productId) {
        return productId.intValue() % CONTAINER_SIZE;
    }

    public ProductDetailVo getProductCache (Long productId) {
        return getProductCacheByHash(productId).get(productId);
    }


    public Map<Long, ProductDetailVo> getProductCacheByHash(Long productId) {
        return container.get(getKeyRange(productId));
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(container)) container.clear();
        logger.info("ProductCacheContainer destroy success ... ");
    }
}
