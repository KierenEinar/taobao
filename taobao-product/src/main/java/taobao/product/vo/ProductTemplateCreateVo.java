package taobao.product.vo;

import java.util.List;

public class ProductTemplateCreateVo {

    private Long productId;
    private String type;
    private ProductTemplateVo template;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProductTemplateVo getTemplate() {
        return template;
    }

    public void setTemplate(ProductTemplateVo template) {
        this.template = template;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class ProductTemplateVo {
        private String group;
        private List<String> params;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }
    }
}
