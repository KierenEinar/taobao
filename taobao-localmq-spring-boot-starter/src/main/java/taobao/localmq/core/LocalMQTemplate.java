package taobao.localmq.core;

public interface LocalMQTemplate  {
    void sendAsync (String destination, Object data, LocalMQSendCallback sendCallback);
}
