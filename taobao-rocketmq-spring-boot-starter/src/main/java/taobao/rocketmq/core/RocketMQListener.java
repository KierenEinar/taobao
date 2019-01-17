package taobao.rocketmq.core;

public interface RocketMQListener<T> {
    void onMessage (T message);
}
