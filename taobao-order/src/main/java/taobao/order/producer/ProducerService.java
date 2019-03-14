package taobao.order.producer;

import taobao.core.vo.InventoryLockIncrVo;
import taobao.core.vo.InventoryWebVo;
import taobao.core.vo.OrderPayVo;
import taobao.order.dto.OrderItemDto;
import taobao.order.model.Order;
import taobao.order.vo.OrderPayWebVo;

import java.util.List;

public interface ProducerService {
    void sendProductStockBackMessage (List<InventoryWebVo> inventoryWebVoList);
    void sendProductStockUnLockWhileTimeout(Order order);
    void sendOrderPayFailed(OrderPayVo orderPayVo);
    void sendInventoryLcckIncrMessage (List<InventoryLockIncrVo> inventoryLockIncrVos);
}
