package taobao.order.task;

import java.util.concurrent.TimeUnit;

public interface DelayQueueTask {
    void put(DelayQueueProcessor delayQueueProcessor, Long delay);
}
