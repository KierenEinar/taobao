package taobao.core.transaction;

public class LocalTransactionContext<T> {

    private T data;

    public LocalTransactionContext(T data) {
        this.data = data;
    }

    public LocalTransactionContext() {}

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
