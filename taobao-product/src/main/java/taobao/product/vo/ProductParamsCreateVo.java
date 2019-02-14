package taobao.product.vo;
import java.util.List;
import java.util.Map;
public class ProductParamsCreateVo {

    private Long productSpecsId;
    private Long productId;
    private String type;
    private List<Params> params;

    public Long getProductSpecsId() {
        return productSpecsId;
    }

    public void setProductSpecsId(Long productSpecsId) {
        this.productSpecsId = productSpecsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Params> getParams() {
        return params;
    }

    public void setParams(List<Params> params) {
        this.params = params;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public static class Params {

        private String group;
        private Map<String, String> param;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Map<String, String> getParam() {
            return param;
        }

        public void setParam(Map<String, String> param) {
            this.param = param;
        }
    }
}
