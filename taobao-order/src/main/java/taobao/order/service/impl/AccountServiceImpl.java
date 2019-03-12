package taobao.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import taobao.core.model.APIResponse;
import taobao.order.model.Order;
import taobao.order.service.AccountService;
@Service
public class AccountServiceImpl implements AccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public APIResponse<String> freezeAmount(Order order) {
        logger.info("冻结账户金额, 订单 -> {}", order);
        return new APIResponse<>("网络异常, 请稍后重试");
    }
}
