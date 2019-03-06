package taobao.order.vo;

import taobao.core.vo.InventoryWebVo;

import java.util.List;

public class OrderWebVo {

    private Long userId;
    private String submitToken;
    private List<InventoryWebVo> details;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSubmitToken() {
        return submitToken;
    }

    public void setSubmitToken(String submitToken) {
        this.submitToken = submitToken;
    }

    public List<InventoryWebVo> getDetails() {
        return details;
    }

    public void setDetails(List<InventoryWebVo> details) {
        this.details = details;
    }
}
