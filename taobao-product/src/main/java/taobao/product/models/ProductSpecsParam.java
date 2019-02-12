package taobao.product.models;

import java.util.Date;

public class ProductSpecsParam {
    private String id;

    private String name;

    private String value;

    private String productId;

    private String productSpecsId;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getProductSpecsId() {
        return productSpecsId;
    }

    public void setProductSpecsId(String productSpecsId) {
        this.productSpecsId = productSpecsId == null ? null : productSpecsId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}