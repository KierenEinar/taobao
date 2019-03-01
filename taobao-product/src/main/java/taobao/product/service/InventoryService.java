package taobao.product.service;

import taobao.product.vo.ProductDetailVo;

import java.util.List;

public interface InventoryService {
    void releaseProductStock2Redis(Long productId);
    Boolean isProductStockPersistRedis(Long productId);
    List<Integer> getStocks(Long productId, List<ProductDetailVo.Specs> specs);
    Boolean incrInventory(Long productId, Long specsId, Long nums);
}
