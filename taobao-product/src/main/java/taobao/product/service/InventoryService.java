package taobao.product.service;

import taobao.core.vo.InventoryWebVo;
import taobao.product.vo.ProductDetailVo;

import java.util.List;

public interface InventoryService {
    void releaseProductStock2Redis(Long productId);
    Boolean isProductStockPersistRedis(Long productId);
    List<Integer> getStocks(Long productId, List<ProductDetailVo.Specs> specs);
    Boolean preIncrInventory(InventoryWebVo inventoryWebVo);
    Boolean batchPreIncrInventory (List<InventoryWebVo> inventoryWebVos);
    Boolean incrInventory (InventoryWebVo inventoryWebVo);
}
