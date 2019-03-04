package taobao.order.vo;

import java.util.List;

public class OrderWebVo {

    private Long userId;
    private String submitToken;
    private List<OrderDetailWebVo> details;

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

    public List<OrderDetailWebVo> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailWebVo> details) {
        this.details = details;
    }

    public class OrderDetailWebVo {
        private Long productId;
        private Integer nums;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getNums() {
            return nums;
        }

        public void setNums(Integer nums) {
            this.nums = nums;
        }
    }

}
