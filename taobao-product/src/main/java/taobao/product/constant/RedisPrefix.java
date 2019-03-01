package taobao.product.constant;

public class RedisPrefix {
    public static String productSpecsStockKey (Long productId) {
        return "product::stock::" + productId;
    }
    public static String productDetailByKey (Long productId) {return "product::detail::" + productId;}

    public static String productExistsKey(Long productId) {return "product::exists::" + productId;}
    public static String productBloomFilterKey = "product::bloomfilter";
}
