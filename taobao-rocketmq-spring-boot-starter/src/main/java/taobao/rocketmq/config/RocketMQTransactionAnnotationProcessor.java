package taobao.rocketmq.config;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;

import java.util.Map;
import java.util.Objects;

public class RocketMQTransactionAnnotationProcessor implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    private TransactionHandlerRegistry transactionHandlerRegistry;

    public RocketMQTransactionAnnotationProcessor (TransactionHandlerRegistry transactionHandlerRegistry) {
        this.transactionHandlerRegistry = transactionHandlerRegistry;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(RocketMQTransactionListener.class);
        if (Objects.nonNull(objectMap)) {
            objectMap.forEach(this::registerTransactionHandler);
        }
    }

    private void registerTransactionHandler (String name, Object bean) {
        TransactionHandler transactionHandler = new TransactionHandler();
        Class<?> clazz = AopUtils.getTargetClass(bean);
        RocketMQTransactionListener rocketMQTransactionListener = clazz.getAnnotation(RocketMQTransactionListener.class);

        transactionHandler.setBeanName(name);
        transactionHandler.setName(rocketMQTransactionListener.producerGroup());
        transactionHandler.setBean((RocketMQLocalTransactionListener) bean);
        transactionHandler.setThreadPoolExecutor(
                rocketMQTransactionListener.corePoolSize(),
                rocketMQTransactionListener.maxiumPoolSize(),
                rocketMQTransactionListener.keepAliveTime(),
                rocketMQTransactionListener.blockingQueueSize());

        transactionHandlerRegistry.register(transactionHandler);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
