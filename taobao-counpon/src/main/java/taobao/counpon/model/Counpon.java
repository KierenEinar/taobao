package taobao.counpon.model;

import taobao.hbase.annotation.hbase.FamilyColumn;
import taobao.hbase.annotation.hbase.HbaseEntity;
import taobao.hbase.annotation.hbase.Qualify;
import taobao.hbase.model.HbaseModel;

import java.util.Date;

@HbaseEntity(table = "counpon")
public class Counpon extends HbaseModel {

    @FamilyColumn("counpon_info")
    @Qualify("vendorId")
    private String vendorId;

    @FamilyColumn("counpon_info")
    @Qualify("count")
    private Long count;

    @FamilyColumn("counpon_info")
    @Qualify("value")
    private Double value;

    @FamilyColumn("counpon_info")
    @Qualify("startTime")
    private Date startTime;

    @FamilyColumn("counpon_info")
    @Qualify("startTime")
    private Date endTime;

    @FamilyColumn("counpon_info")
    @Qualify("description")
    private String description;

    @FamilyColumn("counpon_info")
    @Qualify("status")
    private String status;

    @FamilyColumn("coupon_rule")
    @Qualify("type")
    private String type;

    @FamilyColumn("coupon_rule")
    @Qualify("code")
    private String code;

    @FamilyColumn("coupon_rule")
    @Qualify("minPrice")
    private Double minPrice;

    @FamilyColumn("coupon_rule")
    @Qualify("isOverly")
    private Boolean isOverly;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Boolean getOverly() {
        return isOverly;
    }

    public void setOverly(Boolean overly) {
        isOverly = overly;
    }
}
