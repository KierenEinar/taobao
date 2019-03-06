package taobao.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;

import java.util.List;

@FeignClient(name = "/api/v1/products", fallback = ProductService.class)
public interface ProductService {
    @PostMapping("/speces")
    APIResponse findSpeces(@RequestBody List<InventoryWebVo> vos);
}
