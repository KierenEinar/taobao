package taobao.product.listener;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.core.vo.InventoryWebVo;
import taobao.core.constant.Constant;
import taobao.product.service.InventoryService;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@Service
@RocketMQMessageListener(consumerGroup = Constant.Producer.inventory_update_group, topic = Constant.Topic.inventory_back_topic)
public class ProductStockBackDBListener implements RocketMQListener<InventoryWebVo> {

    @Autowired
    InventoryService inventoryService;

    @Override
    public void onMessage(InventoryWebVo inventoryWebVo) {
        InventoryWebVo temp = new InventoryWebVo();
        BeanUtils.copyProperties(inventoryWebVo, temp);
        temp.setNums(0-temp.getNums());
        //todo 需要做幂等性操作
        Boolean result =  inventoryService.incrInventory(temp);
        if (Boolean.FALSE.equals(result)) throw new RuntimeException("update inventory failed, num -> "+ inventoryWebVo.getNums() +", id -> " + inventoryWebVo.getSpecsId());
    }
}
