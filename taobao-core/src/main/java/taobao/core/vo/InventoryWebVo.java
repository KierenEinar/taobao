package taobao.core.vo;

public class InventoryWebVo {

    private Long productId;
    private Long specsId;
    private Integer nums;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSpecsId() {
        return specsId;
    }

    public void setSpecsId(Long specsId) {
        this.specsId = specsId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }


    @Override
    public String toString() {
        return "InventoryWebVo{" +
                "productId=" + productId +
                ", specsId=" + specsId +
                ", nums=" + nums +
                '}';
    }
}
