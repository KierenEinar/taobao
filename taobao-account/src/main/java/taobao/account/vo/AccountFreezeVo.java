package taobao.account.vo;

import taobao.account.model.AccountTradeLog;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFreezeVo {
    private Long userId;
    private BigDecimal amount;
    private String orderId;
    private Long toUserId;
    private String remark;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AccountTradeLog buildAccountTradeLog() {
        AccountTradeLog accountTradeLog = new AccountTradeLog();
        accountTradeLog.setFromAccountId(userId);
        accountTradeLog.setToAccountId(toUserId);
        accountTradeLog.setBalance(amount);
        accountTradeLog.setOrderId(orderId);
        accountTradeLog.setCreateTime(new Date());
        accountTradeLog.setRemark(remark);
        accountTradeLog.setUserId(userId);
        accountTradeLog.setChannel(AccountTradeLog.Channel.platform);
        accountTradeLog.setStatus(AccountTradeLog.Status.payment);
        return accountTradeLog;
    }
}
