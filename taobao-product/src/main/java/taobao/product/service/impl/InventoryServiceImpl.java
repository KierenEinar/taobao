package taobao.product.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import taobao.core.RedisService;
import taobao.product.cache.InventoryCacheContainer;
import taobao.product.constant.RedisPrefix;
import taobao.product.mapper.ProductSpecsMapper;
import taobao.product.models.ProductSpecs;
import taobao.product.script.LuaScript;
import taobao.product.service.InventoryService;
import taobao.product.service.ProducerService;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    ProductService productService;

    @Autowired
    RedisService redisService;

    @Autowired
    ProductSpecsMapper productSpecsMapper;


    @Autowired
    InventoryCacheContainer inventoryCacheContainer;

    private static final Lock lock = new ReentrantLock();

    Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public void releaseProductStock2Redis(Long productId) {
        //List<ProductSpecs> productSpecs = productSpecsMapper.selectByProductId(productId);
        List<ProductSpecs> productSpecs = productService.findSpecsByProductId(productId);
        Map<Object, Object> map = productSpecs.stream().collect(Collectors.toMap(i -> i.getId() + "", ProductSpecs::getStock));
        redisService.hmset(RedisPrefix.productSpecsStockKey(productId), map);
    }

    @Override
    public Boolean isProductStockPersistRedis(Long productId) {
        return redisService.exists(RedisPrefix.productSpecsStockKey(productId));
    }

    @Override
    public List<Integer> getStocks(Long productId, List<ProductDetailVo.Specs> specs) {

        List<Integer> stocks = inventoryCacheContainer.getStockCache(productId);

        if (!CollectionUtils.isEmpty(stocks)) {
            logger.info("find stocks in cache, productId -> {}, stocks -> {}", productId, stocks);
            return stocks;
        }

        List<String> specsId =  specs.stream().map(i->i.getId()+"").collect(Collectors.toList());

        try{
            lock.lock();
            stocks = redisService.mget(RedisPrefix.productSpecsStockKey(productId), specsId);
            inventoryCacheContainer.putStockCache(productId, stocks);
        }finally {
            lock.unlock();
        }

        return stocks;
    }

    @Override
    public Boolean preIncrInventory(Long productId, Long  specsId, Long nums) {
        String key = RedisPrefix.productSpecsStockKey(productId);
        String lua = LuaScript.incrbyStock(key, specsId+"");
        return redisService.eval(lua, Boolean.class, Lists.newArrayList(key), nums);
    }

    @Override
    public Boolean incrInventory(Long productId, Long specsId, Integer nums) {
        int result = productSpecsMapper.updateInventory(productId, specsId, nums);
        return result > 0;
    }
}
