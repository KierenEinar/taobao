package taobao.product.listener;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.product.constant.Constant;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;

@Service
@RocketMQMessageListener(consumerGroup = Constant.product_consumer_group, topic =  Constant.product_create_topic)
public class ProductListener implements RocketMQListener<Long> {

    Logger logger = LoggerFactory.getLogger(ProductListener.class);

    @Autowired
    ProductService productService;

    @Override
    public void onMessage(Long productId) {
        logger.info("收到消息 -> {}", JSONObject.toJSONString(productId));
        productService.createProduct2Hbase(productId);
    }


}
