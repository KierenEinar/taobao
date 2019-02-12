package taobao.product.models;

import taobao.core.model.MySqlBaseModel;

import java.util.Date;

public class ProductSpecsAttributeKey extends MySqlBaseModel {

    private String name;
    private Date creaeTime;
    private Date updateTime;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreaeTime() {
        return creaeTime;
    }

    public void setCreaeTime(Date creaeTime) {
        this.creaeTime = creaeTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
