package taobao.order.service;

import taobao.order.vo.OrderWebVo;

public interface OrderService {
    Boolean createOrder (OrderWebVo orderWebVo);
}
