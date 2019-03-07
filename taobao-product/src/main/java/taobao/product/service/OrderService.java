package taobao.product.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import taobao.core.model.APIResponse;
import taobao.product.dto.OrderDetail;
import taobao.product.service.impl.OrderServiceImpl;

import java.util.List;

@FeignClient(name = "taobao-order", fallback = OrderServiceImpl.class)
public interface OrderService {
    @GetMapping("/api/v1/orders/details/{id}/{userId}")
    APIResponse<List<OrderDetail>> findDetailsByOrderIdAndUserId(@PathVariable("id") Long id, @PathVariable("userId") Long userId);
}
