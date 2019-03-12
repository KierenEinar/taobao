package taobao.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;
import taobao.order.service.impl.InventoryServiceImpl;

import java.util.List;


@FeignClient(name = "taobao-product", fallback = InventoryServiceImpl.class)
public interface InventoryService {
    @PutMapping("/api/v1/inventory/pre/incr")
    APIResponse<Boolean> preIncrInventory(InventoryWebVo inventoryWebVo);
    @PutMapping("/api/v1/inventory/pre/incr/batch")
    APIResponse<Boolean> batchPreIncrInventory(List<InventoryWebVo> inventories);
    @PutMapping("/api/v1/inventory/incr/batch")
    APIResponse<Boolean> batchIncrInventory(List<InventoryWebVo> inventories);
}
