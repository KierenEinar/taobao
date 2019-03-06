package taobao.order.service;

import taobao.order.model.Orders;
import taobao.order.vo.OrderWebVo;

public interface OrderService {
    Boolean createOrder (OrderWebVo orderWebVo);
    Boolean updateOrderStatus(Long id, String status, String preStatus);
}
