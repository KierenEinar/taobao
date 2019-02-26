package taobao.product.service;

public interface ProducerService {
    void sendCreateProdcut2CacheMessage (Long productId);
    void sendCreateProductMQInTransaction(Long productId);
    void sendDelCacheMsgDelay(Long productId);
}
