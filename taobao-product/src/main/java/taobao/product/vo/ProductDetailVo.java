package taobao.product.vo;

import taobao.product.models.Product;
import taobao.product.models.ProductSpecs;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductDetailVo {

    private Date timestamp;

    private Product product;

    private List<AttrValuePair> attrs;

    private List<Specs> specs;

    public List<Specs> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Specs> specs) {
        this.specs = specs;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<AttrValuePair> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrValuePair> attrs) {
        this.attrs = attrs;
    }

    public static class Specs extends ProductSpecs {

        private java.util.Map<String, Object> kv;

        public Map<String, Object> getKv() {
            return kv;
        }

        public void setKv(Map<String, Object> kv) {
            this.kv = kv;
        }

        @Override
        public String toString() {
            return "Specs{" +
                    "kv=" + kv +
                    '}';
        }
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public static class AttrValuePair {

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "ProductDetailVo{" +
                "timestamp=" + timestamp +
                ", product=" + product +
                ", attrs=" + attrs +
                ", specs=" + specs +
                '}';
    }
}
