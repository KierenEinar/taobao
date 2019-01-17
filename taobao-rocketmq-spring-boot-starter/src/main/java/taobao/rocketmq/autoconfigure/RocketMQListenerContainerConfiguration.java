package taobao.rocketmq.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import taobao.rocketmq.annotation.ConsumeMode;
import taobao.rocketmq.annotation.MessageModel;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.core.RocketMQListener;
import taobao.rocketmq.support.DefaultRocketMQListenerContainer;
import taobao.rocketmq.support.RocketMQListenerContainer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RocketMQListenerContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    private RocketMQProperties rocketMQProperties;

    private StandardEnvironment environment;

    private ObjectMapper objectMapper;


    public RocketMQListenerContainerConfiguration(RocketMQProperties rocketMQProperties, StandardEnvironment environment, ObjectMapper objectMapper) {
        this.rocketMQProperties = rocketMQProperties;
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class);
        if (Objects.nonNull(beans)) {
            beans.forEach(this::registerContainer);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private void registerContainer (String beanName, Object bean) {

        Class<?> clazz = AopUtils.getTargetClass(bean);

        RocketMQMessageListener rocketMQMessageListener = clazz.getAnnotation(RocketMQMessageListener.class);

        validate(rocketMQMessageListener);

        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;

        String containerBeanName = String.format("%s_%s", clazz.getName(), atomicInteger.incrementAndGet());

        genericApplicationContext.registerBean(containerBeanName, RocketMQListenerContainer.class,

                () -> createRocketMQListenerContainer(bean, rocketMQMessageListener));


    }

    private RocketMQListenerContainer createRocketMQListenerContainer(Object bean, RocketMQMessageListener rocketMQMessageListener) {
        DefaultRocketMQListenerContainer defaultRocketMQListenerContainer = new DefaultRocketMQListenerContainer();
        defaultRocketMQListenerContainer.setNameSrv(rocketMQProperties.getNameServer());
        defaultRocketMQListenerContainer.setTopic(environment.resolvePlaceholders(rocketMQMessageListener.topic()));
        defaultRocketMQListenerContainer.setConsumerGroup(environment.resolvePlaceholders(rocketMQMessageListener.consumerGroup()));
        defaultRocketMQListenerContainer.setRocketMQMessageListener(rocketMQMessageListener);
        defaultRocketMQListenerContainer.setRocketMQListener((RocketMQListener<?>) bean);
        defaultRocketMQListenerContainer.setObjectMapper(objectMapper);
        return defaultRocketMQListenerContainer;
    }

    private void validate(RocketMQMessageListener rocketMQMessageListener) {

        if (rocketMQMessageListener.consumeMode() == ConsumeMode.ORDERLY &&
                rocketMQMessageListener.messageModel() == MessageModel.BROADCASTING) {
            throw new BeanDefinitionValidationException("messageModel BROADCASTING dose not support orderly message");
        }
    }

}
