package taobao.order.producer;

import taobao.core.vo.InventoryWebVo;
import taobao.order.dto.OrderItemDto;
import taobao.order.model.Order;

import java.util.List;

public interface ProducerService {
    void sendProductStockBackMessage (List<InventoryWebVo> inventoryWebVoList);
    void sendProductStockUnLockWhileTimeout(Order order);
}
