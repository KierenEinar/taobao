package taobao.product.vo;

import java.util.List;
import java.util.Map;
public class ProductCreateAttrsStockWebVo {

    private Map<String, List<String>> attrs;
    private List<ProductSkuVo> skus;
    private Long productId;
    public Map<String, List<String>> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, List<String>> attrs) {
        this.attrs = attrs;
    }

    public List<ProductSkuVo> getSkus() {
        return skus;
    }

    public void setSkus(List<ProductSkuVo> skus) {
        this.skus = skus;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
