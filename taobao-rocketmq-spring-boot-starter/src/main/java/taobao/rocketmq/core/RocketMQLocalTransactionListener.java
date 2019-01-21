package taobao.rocketmq.core;

import org.apache.rocketmq.common.message.Message;

public interface RocketMQLocalTransactionListener {
    RocketMQLocalTransactionState executeLocalTransaction (final Message message, final Object obj);
    RocketMQLocalTransactionState checkLocalTransaction  (final Message message);
}
