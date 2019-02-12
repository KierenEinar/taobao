package taobao.product.models;

import java.util.Date;

public class ProductSpecsParam {
    private Long id;

    private String name;

    private String value;

    private Long productId;

    private Long productSpecsId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductSpecsId() {
        return productSpecsId;
    }

    public void setProductSpecsId(Long productSpecsId) {
        this.productSpecsId = productSpecsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}