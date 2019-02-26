package taobao.localmq.autoconfigure;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import taobao.localmq.annotation.LocalMQMessageListener;
import taobao.localmq.core.LocalMQListener;
import taobao.localmq.core.LocalQueueContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Configuration
@AutoConfigureAfter(ThreadPoolTaskExecutor.class)
@Import(ThreadPoolTaskExecutor.class)
public class LocalMQListenerContainerConfigure implements ApplicationContextAware, SmartInitializingSingleton {

    ApplicationContext applicationContext;

    Map<String, Integer> containerNameCapacityMap = new ConcurrentHashMap<>();

    Executor executor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LocalMQMessageListener.class);
        beans.forEach(this::registerContainer);
    }

    private void registerContainer(String beanName, Object bean) {

        Class<?> clazz = AopUtils.getTargetClass(bean);

        LocalMQMessageListener localMQMessageListener = clazz.getAnnotation(LocalMQMessageListener.class);

        String topic = localMQMessageListener.topic();
        int capacity = localMQMessageListener.capacity();


        if (containerNameCapacityMap.containsKey(topic)) {
            Assert.isTrue(!containerNameCapacityMap.get(topic).equals(capacity), "topic name is same, but the capacity define different");
        }

        GenericApplicationContext genericApplicationContext = (GenericApplicationContext)applicationContext;

        String containerName = String.format("%s", topic);

        genericApplicationContext.registerBean(containerName,
                LocalQueueContainer.class,
                () -> createLocalQueueContainer(bean, localMQMessageListener));


    }

    private LocalQueueContainer createLocalQueueContainer (Object bean, LocalMQMessageListener localMQMessageListener) {
        LocalQueueContainer localQueueContainer = new LocalQueueContainer();
        localQueueContainer.setExecutor(executor);
        localQueueContainer.setLocalMQListener((LocalMQListener<?>) bean);
        localQueueContainer.setLocalMQMessageListener(localMQMessageListener);
        return localQueueContainer;
    }


}
