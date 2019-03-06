package taobao.order.exception;

public class OrderException extends RuntimeException {
    private String message;
    public OrderException (String message) {
        this.message = message;
    }
}
