package taobao.rocketmq.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import taobao.rocketmq.core.RocketMQListener;

public interface RocketMQListenerContainer extends SmartLifecycle, DisposableBean, InitializingBean {
    void setupRocketMQListener (RocketMQListener<?> rocketMQListener);
}
