package taobao.rocketmq.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RocketMQTransactionListener {

    String producerGroup() default Constant.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME;

    int corePoolSize () default 1;

    int maxiumPoolSize () default 1;

    long keepAliveTime () default 60 * 1000L;

    int blockingQueueSize () default 2000;
}


