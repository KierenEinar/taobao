package taobao.product.service;

import taobao.product.models.ProductSpecs;

public interface ProducerService {
    void sendCreateProdcut2CacheMessage (Long productId);
    void sendCreateProductMQInTransaction(Long productId);
    void sendDelCacheMsgDelay(Long productId);
//    void sendInventoryUpdateMessage (ProductSpecs productSpecs);
}
