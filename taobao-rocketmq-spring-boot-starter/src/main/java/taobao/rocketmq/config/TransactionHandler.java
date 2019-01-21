package taobao.rocketmq.config;

import taobao.rocketmq.annotation.RocketMQTransactionListener;
import taobao.rocketmq.core.RocketMQLocalTransactionListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransactionHandler {

    private RocketMQLocalTransactionListener bean;

    private ThreadPoolExecutor threadPoolExecutor;

    private String name;

    private String beanName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public RocketMQLocalTransactionListener getBean() {
        return bean;
    }

    public void setBean(RocketMQLocalTransactionListener bean) {
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setThreadPoolExecutor(int corePoolSize,
                                      int maximumPoolSize,
                                      long keepAliveTime,
                                      int blockingQueueSize) {
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(blockingQueueSize)
                );
    }
}
