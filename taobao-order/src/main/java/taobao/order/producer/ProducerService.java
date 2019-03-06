package taobao.order.producer;

import taobao.core.vo.InventoryWebVo;

import java.util.List;

public interface ProducerService {
    void sendProductStockBackMessage (List<InventoryWebVo> inventoryWebVoList);
    void sendProductStockUnLockWhileTimeout(Long id, List<InventoryWebVo> details);
}
