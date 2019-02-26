package taobao.localmq.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;
import taobao.localmq.annotation.LocalMQMessageListener;
import taobao.localmq.support.LocalQueueBody;

import java.util.Map;
import java.util.concurrent.*;

public class LocalQueueContainer implements SmartLifecycle, DisposableBean, InitializingBean {

    LocalMQListener localMQListener;

    LocalMQMessageListener localMQMessageListener;

    Map<Integer, BlockingQueue<LocalQueueBody>> partitionQueue = new ConcurrentHashMap<>();

    int maxPartition;

    int capacity;

    Boolean running;

    Logger logger = LoggerFactory.getLogger(LocalQueueContainer.class);

    Executor executor;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalMQListener getLocalMQListener() {
        return localMQListener;
    }

    public void setLocalMQListener(LocalMQListener localMQListener) {
        this.localMQListener = localMQListener;
    }

    public LocalMQMessageListener getLocalMQMessageListener() {
        return localMQMessageListener;
    }

    public void setLocalMQMessageListener(LocalMQMessageListener localMQMessageListener) {
        this.localMQMessageListener = localMQMessageListener;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public BlockingQueue routing (int hash) {
        return partitionQueue.get(hash % this.maxPartition);
    }

    @Override
    public void destroy() throws Exception {
        partitionQueue.clear();
        logger.info("LocalMQListenerContainer destroy ... ");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.capacity = localMQMessageListener.capacity();
        this.maxPartition = localMQMessageListener.maxPartition();
        this.name = localMQMessageListener.topic();
        initPartitionQueue(capacity, maxPartition);
    }

    private void initPartitionQueue(int capacity, int maxPartition) {

        for (int i=0 ;i<maxPartition; i++) {
            BlockingQueue blockingQueue = new ArrayBlockingQueue(capacity);
            partitionQueue.put(i, blockingQueue);
        }
    }

    @Override
    public boolean isAutoStartup() {
        return running;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    @Override
    public void start() {
       this.running = Boolean.TRUE;
       for (int i=0; i<this.maxPartition; i++) {
            BlockingQueue blockingQueue = partitionQueue.get(i);
            executor.execute(()->{
                while (true) {
                    try {
                        Object data = blockingQueue.take();
                        localMQListener.onMessage(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
       }
    }

    @Override
    public void stop() {
        if (this.running) {
            logger.info("LocalMQListenerContainer stop ... ");
            if (CollectionUtils.isEmpty(partitionQueue)) {
                partitionQueue.forEach((k,v)->v.clear());
            }
            this.running = Boolean.FALSE;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }

}
