package taobao.order.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import taobao.core.model.APIResponse;
import taobao.core.vo.InventoryWebVo;
import taobao.order.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public APIResponse findSpeces(List<InventoryWebVo> vos) {
        logger.error("获取商品详情 -> {}", vos);
        return new APIResponse(Lists.newArrayList());
    }
}
