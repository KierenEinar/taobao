package taobao.product.service;

public interface InventoryService {
    void releaseProductStock2Redis(Long productId);
    Boolean isProductStockPersistRedis(Long productId);
}
