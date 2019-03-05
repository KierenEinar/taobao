package taobao.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import taobao.core.model.APIResponse;
import taobao.order.service.impl.InventoryServiceImpl;
import taobao.order.vo.InventoryWebVo;


@FeignClient(name = "taobao-product", fallback = InventoryServiceImpl.class)
public interface InventoryService {
    @PutMapping("/api/v1/inventory/pre/incr")
    APIResponse<Boolean> preIncrInventory(InventoryWebVo inventoryWebVo);
}
