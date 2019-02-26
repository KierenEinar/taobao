package taobao.localmq.core;

public interface LocalMQSendCallback {
    void onSuccess(String msgId);
    void onException (Exception e);
}
