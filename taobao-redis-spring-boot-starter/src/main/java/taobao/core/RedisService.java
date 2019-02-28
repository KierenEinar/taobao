package taobao.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisService implements InitializingBean {

    RedisTemplate redisTemplate;

    Logger logger = LoggerFactory.getLogger(RedisService.class);

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value) {
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.set(key.getBytes(), value.getBytes());
            }
        });
    }

    public Boolean set(String key, String value, long ttl, TimeUnit timeUnit) {
         return (Boolean)redisTemplate.execute((RedisConnection redisConnection) -> redisConnection.set(key.getBytes(), value.getBytes(), Expiration.from(ttl, timeUnit), RedisStringCommands.SetOption.UPSERT));
    }

    public Boolean setNX(String key, String value) {
        return (Boolean) redisTemplate.execute((RedisConnection connection)-> connection.setNX(key.getBytes(), value.getBytes()));
    }

    public Boolean setNX(String key, String value, long ttl, TimeUnit timeUnit) {
        return (Boolean) redisTemplate.execute((RedisConnection connection)->connection.set(key.getBytes(), value.getBytes(), Expiration.from(ttl, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    public String get(String key) {
        byte[] bytes = (byte[])redisTemplate.execute((RedisConnection connection)->connection.get(key.getBytes()));
        if (Objects.isNull(bytes)) return null;
        return new String(bytes);
    }

    public Boolean hmset(String key, java.util.Map<Object, Object> map) {
        logger.info("redis hmset, key -> {}, value -> {}", key, map);
        redisTemplate.opsForHash().putAll(key, map);
        return Boolean.TRUE;
    }

    public List mget(String key, List hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public Long hincr(String key, String field) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Long value = hashOperations.increment(key, field, 1L);
        return value;
    }

    public Long hincrby(String key, String field, Long delta) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Long value =  hashOperations.increment(key, field, delta);
        return value;
    }

    public Double hincrby(String key, String field, Double delta) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Double value = hashOperations.increment(key, field, delta);
        return value;
    }

    public Boolean exists (String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    }



}
