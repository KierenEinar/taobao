package taobao.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;
import taobao.product.service.InventoryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @RequestMapping( value = "/pre/incr", method = RequestMethod.PUT)
    public APIResponse<Boolean> preIncrInventory (@RequestBody InventoryWebVo inventory) {
        logger.info("begin preIncrInventory ...");
        Boolean result = inventoryService.preIncrInventory(inventory);
        logger.info("end preIncrInventory..., result -> {}", result);
        return new APIResponse<>(result);
    }

    @RequestMapping( value = "/incr", method = RequestMethod.PUT)
    public APIResponse<Boolean> incrInventory (@RequestBody InventoryWebVo inventory) {
        logger.info("begin incrInventory ...");
        Boolean result = inventoryService.incrInventory(inventory);
        logger.info("end incrInventory...");
        return new APIResponse<>(result);
    }

}
