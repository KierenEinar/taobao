package taobao.product.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import taobao.core.RedisService;
import taobao.core.vo.InventoryLockIncrVo;
import taobao.product.cache.InventoryCacheContainer;
import taobao.product.constant.RedisPrefix;
import taobao.core.vo.InventoryWebVo;
import taobao.product.exception.ProductEventException;
import taobao.product.mapper.ProductSpecsLocknumLogMapper;
import taobao.product.mapper.ProductSpecsMapper;
import taobao.product.models.ProductSpecs;
import taobao.product.models.ProductSpecsLocknumLog;
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
    ProducerService producerService;

    @Autowired
    RedisService redisService;

    @Autowired
    ProductSpecsMapper productSpecsMapper;

    @Autowired
    InventoryCacheContainer inventoryCacheContainer;

    @Autowired
    ProductSpecsLocknumLogMapper productSpecsLocknumLogMapper;

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
    public Boolean preIncrInventory(InventoryWebVo inventoryWebVo) {
        String key = RedisPrefix.productSpecsStockKey(inventoryWebVo.getProductId());
        String lua = LuaScript.incrbyStock(key, inventoryWebVo.getSpecsId()+"");
        logger.info("lua -> {}, key-> {}, num -> {}", lua, key, inventoryWebVo.getNums());
        return redisService.eval(lua, Boolean.class, Lists.newArrayList(key), inventoryWebVo.getNums());
    }

    @Override
    public Boolean batchPreIncrInventory(List<InventoryWebVo> inventoryWebVos) {

        List<InventoryWebVo> stockNotEnough = Lists.newArrayList();

        Boolean flag = Boolean.FALSE;

        for (InventoryWebVo vo: inventoryWebVos) {
            Boolean result = incrInventory(vo);
            logger.info("batchPreIncrInventory, result -> {}", result);
            if (Boolean.TRUE.equals(result)) stockNotEnough.add(vo);
            else {
                flag = Boolean.TRUE;
                break;
            }
        }

        if (Boolean.FALSE.equals(flag)) return Boolean.TRUE;

        //发送事务消息, 回退被扣调的库存

        producerService.sendProductStockBackMessage(stockNotEnough);

        return Boolean.FALSE;
    }

    @Override
    public Boolean incrInventory(InventoryWebVo inventoryWebVo) {
        try{
            int result = productSpecsMapper.updateInventory(inventoryWebVo.getProductId(), inventoryWebVo.getSpecsId(), inventoryWebVo.getNums());
            return result > 0;
        }catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    @Override
    @Transactional
    public Boolean incrLockInventory(InventoryLockIncrVo inventoryLockIncrVo) {
        ProductSpecsLocknumLog productSpecsLocknumLog = new ProductSpecsLocknumLog();
        BeanUtils.copyProperties(inventoryLockIncrVo, productSpecsLocknumLog);
        if( null!= productSpecsLocknumLogMapper.selectByPrimaryKey(productSpecsLocknumLog.getId())) {
            logger.info("重复消费=======, productSpecsLocknumLog -> {}", inventoryLockIncrVo);
        }
        int result = productSpecsLocknumLogMapper.insert(productSpecsLocknumLog);
        if (result == 0) throw new ProductEventException("系统繁忙, 稍后重试");
        result = productSpecsMapper.updateLockInventory (inventoryLockIncrVo.getIncrLockNum(), inventoryLockIncrVo.getProductId(), inventoryLockIncrVo.getSpecsId());
        if (result == 0) throw new ProductEventException("修改被锁库存失败, 请检查锁库存的数量");
        return Boolean.TRUE;
    }

}
