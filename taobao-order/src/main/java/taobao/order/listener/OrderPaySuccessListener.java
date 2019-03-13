package taobao.order.listener;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.constant.Constant;
import taobao.core.vo.OrderPayVo;
import taobao.order.service.OrderService;
import taobao.order.vo.OrderPayWebVo;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;
@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.order_group, topic = Constant.Topic.pay_success_notify_order)
public class OrderPaySuccessListener implements RocketMQListener<String> {

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(String json) {
        OrderPayVo orderPayVo = JSONObject.parseObject(json, OrderPayVo.class);
        orderService.payOrder(orderPayVo);
    }
}
