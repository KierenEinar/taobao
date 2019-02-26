package taobao.localmq.support;

import taobao.localmq.core.LocalMQSendCallback;

public class LocalQueueBody {
    public LocalQueueBody(Object data, LocalMQSendCallback localMQSendCallback) {
        this.data = data;
        this.localMQSendCallback = localMQSendCallback;
    }

    private Object data;

    private LocalMQSendCallback localMQSendCallback;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalMQSendCallback getLocalMQSendCallback() {
        return localMQSendCallback;
    }

    public void setLocalMQSendCallback(LocalMQSendCallback localMQSendCallback) {
        this.localMQSendCallback = localMQSendCallback;
    }
}
