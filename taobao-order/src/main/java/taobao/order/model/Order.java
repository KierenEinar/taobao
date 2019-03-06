package taobao.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

    public interface Status {
        String unpaying = "unpaying";
        String paied    = "paied";
        String shipped  = "shipped";
        String finished = "finished";
        String canceled = "canceled";
        String delivered= "delivered";
        String timeout = "timeout";
    }

    public interface PayCHannel {
        String zhifubao = "zhifubao";
        String weixin   = "weixin";
        String platform = "platform";
    }

    private Long id;

    private BigDecimal totalCost;

    private Long userId;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String payChannel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel == null ? null : payChannel.trim();
    }
}