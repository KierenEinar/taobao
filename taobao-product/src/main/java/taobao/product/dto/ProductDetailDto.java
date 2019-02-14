package taobao.product.dto;

import taobao.product.models.Product;

public class ProductDetailDto extends Product {

    private String attrKey;

    public String getAttrKey() {
        return attrKey;
    }

    public void setAttrKey(String attrKey) {
        this.attrKey = attrKey;
    }
}
