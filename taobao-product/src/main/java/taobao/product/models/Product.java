package taobao.product.models;

import taobao.core.model.MySqlBaseModel;

import java.util.Date;

public class Product extends MySqlBaseModel {

    private String name;
    private String title;
    private String html;
    private Date creaeTime;
    private Date updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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
