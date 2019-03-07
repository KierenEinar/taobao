package taobao.product.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import taobao.core.model.APIResponse;
import taobao.product.dto.OrderDetail;
import taobao.product.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public APIResponse<List<OrderDetail>> findDetailsByOrderIdAndUserId(Long id, Long userId) {
        logger.info("call web service findDetailsByOrderId failed, id -> {}, userId -> {}", id, userId);
        return new APIResponse(Lists.newArrayList());
    }
}
