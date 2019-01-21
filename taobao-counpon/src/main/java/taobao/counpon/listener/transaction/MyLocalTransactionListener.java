package taobao.counpon.listener.transaction;

import org.apache.rocketmq.common.message.Message;
import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionState;

@RocketMQTransactionListener
public class MyLocalTransactionListener implements RocketMQLocalTransactionListener {


    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object obj) {
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        return RocketMQLocalTransactionState.COMMIT;
    }
}
