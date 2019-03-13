package taobao.core.constant;

public class Constant {

    public interface Producer {
        String product_create_group = "taobao-product-create-group";
        String product_create_redis_transaction_group = "product-create-redis-transaction-group";
        String product_create_redis_group = "product-create-redis-group";
        String product_detail_cache_group = "product-detail-cache-group";
        String inventory_update_group = "inventory-update-group";
        String order_group = "order-group";
        String order_transaction_group = "order-transaction-group";
        String account_group = "account_group";
    }

    public interface TransactionProducer {

        String product_release_group = "product-release-group";
        String product_order_timeout_group = "product-order-timeout-group";
    }

    public interface Topic {
        String product_create_topic = "taobao-product-create-topic";
        String product_create_redis_transaction_topic = "product-create-redis-transaction-topic";
        String product_create_redis_topic = "product-create-redis-topic";
        String product_detail_cache_del_topic = "product-detail-cache-del-topic";
        String product_create_local_cache_topic = "product-create-local-cache-topic";
        String inventory_update_topic = "inventory-update-topic";
        String inventory_back_topic = "inventory-back-topic";
        String order_timeout_topic = "order_timeout-topic";
        String pay_success_notify_order = "pay-success-notify-order";
        String order_update_failed_topic = "order-update-failed-topic";
        String account_refund_topic = "account-refund-topic";
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
