package taobao.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;
import taobao.order.dto.ProductSpecesDto;
import taobao.order.service.impl.ProductServiceImpl;

import java.util.List;

@FeignClient(name = "taobao-product", fallback = ProductServiceImpl.class)
public interface ProductService {
    @PostMapping("/api/v1/products/speces")
    APIResponse<List<ProductSpecesDto>> findSpeces(List<InventoryWebVo> vos);
}
