package taobao.order.service;
import taobao.core.vo.OrderPayVo;
import taobao.order.model.OrderDetail;
import taobao.order.vo.OrderPayWebVo;
import taobao.order.vo.OrderWebVo;

import java.util.List;

public interface OrderService {
    Boolean createOrder (OrderWebVo orderWebVo);
    Boolean updateOrderStatus(Long id, String status, String preStatus,  Long userId);
    String findOrderStatus(Long id, Long userId);
    List<OrderDetail> findDetails(Long id, Long userId);
    Boolean payOrder (OrderPayVo orderPayVo);
}
