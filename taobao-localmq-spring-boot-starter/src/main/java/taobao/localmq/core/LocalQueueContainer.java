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

    boolean running;

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
        logger.info("LocalMQListenerContainer destroy ... ");
        partitionQueue.clear();
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
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    @Override
    public void start() {
       logger.info("LocalQueueContainer start, name -> {}", name);
       for (int i=0; i<this.maxPartition; i++) {
            BlockingQueue<LocalQueueBody> blockingQueue = partitionQueue.get(i);
            executor.execute(()->{
                while (true) {
                    LocalMQSendCallback localMQSendCallback = null;
                    try {
                        LocalQueueBody data = blockingQueue.take();
                        localMQSendCallback = data.getLocalMQSendCallback();
                        localMQSendCallback.onSuccess("");
                        localMQListener.onMessage(data.getData());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        localMQSendCallback.onException(e);
                    }
                }
            });
       }
        this.running = Boolean.TRUE;
    }

    @Override
    public void stop() {
        logger.info("LocalMQListenerContainer stop ... ");
        if (this.running) {
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
        return Integer.MAX_VALUE;
    }

}
