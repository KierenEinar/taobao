package taobao.product.script;

public interface LuaScript {
     static String incrbyStock(String key, String field) {
        String script = "local result = redis.call('hget', '%s', '%s') " +
                        "if tonumber(result) <= 0 then " +
                        "return false " +
                        "end " +
                        "result = redis.call('hincrby', '%s', '%s', ARGV[1]) " +
                        "if result >= 0 then " +
                        "return true " +
                        "end " +
                        "return false";

        return String.format(script, key, field, key, field);
    }
}
