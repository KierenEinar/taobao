package taobao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taobao.core.model.APIResponse;
import taobao.order.service.OrderService;
import taobao.order.vo.OrderWebVo;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity<?> create (@RequestBody OrderWebVo orderWebVo) {
        return ResponseEntity.ok(new APIResponse<>(orderService.createOrder(orderWebVo)));
    }

}
