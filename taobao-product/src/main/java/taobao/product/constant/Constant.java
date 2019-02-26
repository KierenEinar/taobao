package taobao.product.constant;

public class Constant {

    public interface Producer {
        String product_create_group = "taobao-product-create-group";
        String product_create_redis_transaction_group = "product-create-redis-transaction-group";
        String product_create_redis_group = "product-create-redis-group";
        String product_detail_cache_group = "product-detail-cache-group";
    }

    public interface Topic {
        String product_create_topic = "taobao-product-create-topic";
        String product_create_redis_transaction_topic = "product-create-redis-transaction-topic";
        String product_create_redis_topic = "product-create-redis-topic";
        String product_detail_cache_del_topic = "product-detail-cache-del-topic";
        String product_create_local_cache_topic = "product-create-local-cache-topic";
    }

    public static class HashKey {
        public static String product_create_hash_key (Long productId) {
            return "product_create_hash_key:" + productId;
        }
    }

    public interface Timeout {
        Long product_detail_cache_timeout = 5000L;
    }

}
