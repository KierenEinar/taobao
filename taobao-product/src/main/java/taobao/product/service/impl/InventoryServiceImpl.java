package taobao.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.RedisService;
import taobao.product.constant.RedisPrefix;
import taobao.product.models.ProductSpecs;
import taobao.product.service.InventoryService;
import taobao.product.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    ProductService productService;

    @Autowired
    RedisService redisService;

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
}
