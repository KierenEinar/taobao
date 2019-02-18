package taobao.product.constant;

public interface HbaseConstant {

    interface Product {
        String table = "product";
        String Info = "info";
        String attrs = "attrs";
        String specs = "specs";

        interface CFInfo {
            String title = "title";
            String name = "name";
            String html = "html";
        }

        interface CFSpecs {
            String attrs = "attrs";
            String price = "price";
            String id = "id";
            String kv_params = "kv-params";
            String kv_info = "kv-info";
        }


    }



}
