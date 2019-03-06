package taobao.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import taobao.core.model.APIResponse;
import taobao.order.service.InventoryService;
import taobao.order.vo.InventoryWebVo;

import java.util.List;


@Component
public class InventoryServiceImpl implements InventoryService {


    Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public APIResponse<Boolean> preIncrInventory(InventoryWebVo inventoryWebVo) {
        logger.info("调用预扣库存接口失败");
        return new APIResponse<>(Boolean.FALSE);
    }

    @Override
    public APIResponse<Boolean> batchLockInventorys(List<InventoryWebVo> inventories) {
        logger.info("锁库存接口调用失败");
        return new APIResponse<>(Boolean.FALSE);
    }
}
