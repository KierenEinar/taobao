package taobao.account.listener;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.account.service.AccountService;
import taobao.account.vo.AccountFreezeVo;
import taobao.core.constant.Constant;
import taobao.core.vo.OrderPayVo;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;
@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.account_group, topic = Constant.Topic.account_refund_topic)
public class AccountRefundMessageListener implements RocketMQListener<String> {

    @Autowired
    AccountService accountService;

    @Override
    public void onMessage(String json) {
        OrderPayVo orderPayVo = JSONObject.parseObject(json, OrderPayVo.class);
        accountService.refund(orderPayVo);
    }
}
