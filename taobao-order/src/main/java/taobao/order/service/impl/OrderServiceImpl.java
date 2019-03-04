package taobao.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.order.mapper.OrdersMapper;
import taobao.order.service.OrderService;
import taobao.order.vo.OrderWebVo;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrdersMapper ordersMapper;

    @Override
    public Boolean createOrder(OrderWebVo orderWebVo) {

        //检查重复提交

        //redis 预扣库存

        //生成订单

        //等待付款, 生成延时消息队列(如果没有在规定时间内付款则恢复redis库存)


        return Boolean.TRUE;
    }
}
