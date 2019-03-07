package taobao.order.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class DefaultDelayQueueTask implements DelayQueueTask, InitializingBean, DisposableBean {

    @Autowired
    ThreadPoolTaskExecutor executor;

    @Autowired
    DelayQueue<Message> delayQueue;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));

    Logger logger = LoggerFactory.getLogger(DefaultDelayQueueTask.class);

    @Override
    public void put(DelayQueueProcessor delayQueueProcessor, Long delay) {
        delayQueue.offer(new Message(delayQueueProcessor, delay));
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        threadPoolExecutor.execute(()->{
            while (true) {
                try {
                    Message message = delayQueue.take();
                    logger.info("啦啦啦, -> {}", message.getDelayQueueProcessor());
                    executor.execute(message.getDelayQueueProcessor());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        if (!threadPoolExecutor.isShutdown()) threadPoolExecutor.shutdown();
        executor.destroy();
    }


    private class Message implements Delayed {

        DelayQueueProcessor delayQueueProcessor;

        Logger logger = LoggerFactory.getLogger(Message.class);

        Long delay;

        Long executeTime;

        public DelayQueueProcessor getDelayQueueProcessor() {
            return delayQueueProcessor;
        }

        public void setDelayQueueProcessor(DelayQueueProcessor delayQueueProcessor) {
            this.delayQueueProcessor = delayQueueProcessor;
        }

        public Long getDelay() {
            return delay;
        }

        public void setDelay(Long delay) {
            this.delay = delay;
        }

        public Long getExecuteTime() {
            return executeTime;
        }

        public void setExecuteTime(Long executeTime) {
            this.executeTime = executeTime;
        }

        public Message(DelayQueueProcessor delayQueueProcessor, Long delay) {
            this.delayQueueProcessor = delayQueueProcessor;
            this.delay = delay;
            this.executeTime = TimeUnit.NANOSECONDS.convert(delay, TimeUnit.MILLISECONDS) + System.nanoTime();
            logger.info("executeTime ->{}", executeTime);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            Long result = executeTime - System.nanoTime();
            logger.info("delay -> {}", result);
            return result ;
        }

        @Override
        public int compareTo(Delayed o) {
            long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
            if (result > 0) return 1;
            else if (result < 0) return -1;
            return 0;
        }

    }


}
