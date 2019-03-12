package taobao.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import taobao.core.model.APIResponse;
import taobao.order.model.Order;
import taobao.order.service.impl.AccountServiceImpl;

@FeignClient(name = "taobao-account", fallback = AccountServiceImpl.class)
public interface AccountService {
    APIResponse<String> freezeAmount(Order order);
}
