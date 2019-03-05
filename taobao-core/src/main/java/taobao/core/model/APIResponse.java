package taobao.core.model;

public class APIResponse<T> {

    private Integer code;
    private T data;

    public APIResponse (Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public APIResponse() {}

    public APIResponse (T data) {
        this(200, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
