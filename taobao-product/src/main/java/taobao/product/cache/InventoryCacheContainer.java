package taobao.product.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryCacheContainer implements DisposableBean {

    @Autowired
    Cache<Long, List<Integer>> stockCache;

    Logger logger = LoggerFactory.getLogger(InventoryCacheContainer.class);

    public void putStockCache (Long productId, List<Integer> stocks) {
        logger.info("put stock into cache, productId -> {}, stocks -> {}", productId, stocks);
        stockCache.put(productId, stocks);
    }

    public List<Integer> getStockCache (Long productId) {
        return stockCache.getIfPresent(productId);
    }


    @Override
    public void destroy() throws Exception {
        stockCache.cleanUp();
    }
}
