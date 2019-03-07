package taobao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taobao.core.model.APIResponse;
import taobao.order.model.OrderDetail;
import taobao.order.service.OrderService;
import taobao.order.vo.OrderWebVo;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity<?> create (@RequestBody OrderWebVo orderWebVo) {
        return ResponseEntity.ok(new APIResponse<>(orderService.createOrder(orderWebVo)));
    }

    @RequestMapping(value = "/details/{id}/{userId}", method = RequestMethod.GET)
    APIResponse<List<OrderDetail>> findDetails (@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return new APIResponse<>(orderService.findDetails(id, userId));
    }

}
