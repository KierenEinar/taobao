package taobao.core.vo;

public class OrderPayVo {

    private Long userId;
    private String orderId;
    private Long tradeNo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Long tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public String toString() {
        return "OrderPayVo{" +
                "userId=" + userId +
                ", orderId='" + orderId + '\'' +
                ", tradeNo=" + tradeNo +
                '}';
    }
}
