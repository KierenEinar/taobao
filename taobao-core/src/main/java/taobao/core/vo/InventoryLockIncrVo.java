package taobao.core.vo;

import java.util.Date;

public class InventoryLockIncrVo {
    private Long id;

    private Date createTime;

    private Long specsId;

    private Integer incrLockNum;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getSpecsId() {
        return specsId;
    }

    public void setSpecsId(Long specsId) {
        this.specsId = specsId;
    }

    public Integer getIncrLockNum() {
        return incrLockNum;
    }

    public void setIncrLockNum(Integer incrLockNum) {
        this.incrLockNum = incrLockNum;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
