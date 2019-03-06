package taobao.product.service;

import taobao.core.vo.InventoryWebVo;
import taobao.product.models.ProductSpecs;

import java.util.List;

public interface ProducerService {
    void sendCreateProdcut2CacheMessage (Long productId);
    void sendCreateProductMQInTransaction(Long productId);
    void sendDelCacheMsgDelay(Long productId);
//    void sendInventoryUpdateMessage (ProductSpecs productSpecs);
    void sendProductStockBackMessage (List<InventoryWebVo> inventoryWebVoList);
}
