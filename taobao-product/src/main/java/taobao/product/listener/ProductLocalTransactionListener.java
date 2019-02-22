package taobao.product.listener;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import taobao.product.constant.Constant;
import taobao.product.service.ProductService;
import taobao.product.vo.ProductDetailVo;
import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionState;

@RocketMQTransactionListener
public class ProductRedisCreateTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductRedisCreateTransactionListener.class);

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object obj) {
        Long productId = getProductIdByMessage(message);
        logger.info("execute executeLocalTransaction, productId -> {}", productId);
        ProductDetailVo productDetailVo = productService.findProductDetailFromDB(productId);
        productService.createProduct2Redis(productId, productDetailVo);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        Long productId = getProductIdByMessage(message);
        logger.info("execute checkLocalTransaction, productId -> {}", productId);
        if (productService.isProductDetailExistsFromRedis(productId)) return RocketMQLocalTransactionState.COMMIT;
        return RocketMQLocalTransactionState.UNKNOWN;
    }



    private Long getProductIdByMessage (Message message) {
        Long productId = Long.parseLong(Bytes.toString(message.getBody()));
        return productId;
    }


}
