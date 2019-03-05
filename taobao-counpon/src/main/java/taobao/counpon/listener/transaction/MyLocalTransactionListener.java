package taobao.counpon.listener.transaction;

import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionState;

@RocketMQTransactionListener
public class MyLocalTransactionListener implements RocketMQLocalTransactionListener {

    Logger logger = LoggerFactory.getLogger(MyLocalTransactionListener.class);

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object obj) {
        logger.info("executeLocalTransaction .... ");
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        logger.info("checkLocalTransaction .... ");
        return RocketMQLocalTransactionState.COMMIT;
    }
}
