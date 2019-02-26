package taobao.localmq.core;

public interface LocalMQListener<T> {
    void onMessage(T data);
}
