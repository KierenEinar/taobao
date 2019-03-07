package taobao.product.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetail {
    private Long id;

    private Long orderId;

    private Long productId;

    private Long userId;

    private Integer quantity;

    private BigDecimal price;

    private Date createTime;

    private Date updateTime;

    private Long productSpecsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getProductSpecsId() {
        return productSpecsId;
    }

    public void setProductSpecsId(Long productSpecsId) {
        this.productSpecsId = productSpecsId;
    }
}
