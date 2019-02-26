package taobao.product.listener;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import taobao.product.service.ProductService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionState;


@RocketMQTransactionListener
public class ProductLocalTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductLocalTransactionListener.class);

    Map<Long, Boolean> cache = new ConcurrentHashMap<>();

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object obj) {
        Long productId = getProductIdByMessage(message);
        RocketMQLocalTransactionState state = RocketMQLocalTransactionState.UNKNOWN;
        logger.info("executeLocalTransaction, productId -> {}", productId);
        try {
            cache.put(productId, Boolean.FALSE);
            productService.releaseProduct2DB(productId);
        } catch (Exception e) {
            cache.remove(productId);
            state = RocketMQLocalTransactionState.ROLLBACK;
        }
        state = RocketMQLocalTransactionState.COMMIT;
        cache.put(productId, Boolean.TRUE);
        return state;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {

        Long productId = getProductIdByMessage(message);
        logger.info("executeLocalTransaction, productId -> {}", productId);
        if (!cache.containsKey(productId)) return RocketMQLocalTransactionState.ROLLBACK;
        Boolean result = cache.get(productId);
        if (Boolean.TRUE.equals(result)) return RocketMQLocalTransactionState.COMMIT;
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    private Long getProductIdByMessage(Message message) {
        Long productId = Long.parseLong(Bytes.toString(message.getBody()));
        return productId;
    }


}
