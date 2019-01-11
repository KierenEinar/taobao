package taobao.counpon.model;

import taobao.hbase.annotation.hbase.FamilyColumn;
import taobao.hbase.annotation.hbase.HbaseEntity;
import taobao.hbase.annotation.hbase.Qualify;
import taobao.hbase.model.HbaseModel;

import java.util.Date;
import java.util.List;

@HbaseEntity(table = "user_counpon")
public class UsersCounpon extends HbaseModel {

    @FamilyColumn("user")
    @Qualify("id")
    private Long userId;

    @FamilyColumn("vendor")
    @Qualify("id")
    private Long vendorId;

    @FamilyColumn("info")
    @Qualify("couponId")
    private Long couponId;

    @FamilyColumn("Info")
    @Qualify("status")
    private String status;

    @FamilyColumn("Info")
    @Qualify("usedTime")
    private Date usedTime;

    @FamilyColumn(value = "product_info")
    @Qualify(type = Qualify.Type.mulit, prefix = "productId-col")
    private List <String> productIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    @Override
    public String toString() {
        return "UsersCounpon{" +
                "userId=" + userId +
                ", vendorId=" + vendorId +
                ", couponId=" + couponId +
                ", status='" + status + '\'' +
                ", usedTime=" + usedTime +
                ", productIds=" + productIds +
                '}';
    }
}
