package taobao.product.listener;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.constant.Constant;
import taobao.core.vo.InventoryLockIncrVo;
import taobao.product.service.InventoryService;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.inventory_lock_update_group, topic = Constant.Topic.inventory_lock_incr_topic)
public class InventoryLockIncrListener implements RocketMQListener<String> {

    @Autowired
    InventoryService inventoryService;

    @Override
    public void onMessage(String json) {
        InventoryLockIncrVo inventoryWebVo = JSONObject.parseObject(json, InventoryLockIncrVo.class);
        inventoryService.incrLockInventory(inventoryWebVo);
    }
}
