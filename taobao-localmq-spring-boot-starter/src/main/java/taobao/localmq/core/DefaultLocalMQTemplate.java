package taobao.localmq.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import taobao.localmq.support.LocalQueueBody;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultLocalMQTemplate implements LocalMQTemplate, ApplicationContextAware {

    ApplicationContext applicationContext;

    Map<String, LocalQueueContainer> cache = new ConcurrentHashMap<>();

    @Override
    public void sendAsync(String destination, Object data, LocalMQSendCallback sendCallback) {
        if (!cache.containsKey(destination)) {
            LocalQueueContainer container = (LocalQueueContainer)applicationContext.getBean(destination);
            cache.put(destination, container);
        }

        LocalQueueContainer container = cache.get(destination);

        if (Objects.isNull(container)) throw new RuntimeException("can't find queue topic -> " + destination);
        BlockingQueue blockingQueue = container.routing(data.hashCode());
        try {
            blockingQueue.put(new LocalQueueBody(data, sendCallback));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
