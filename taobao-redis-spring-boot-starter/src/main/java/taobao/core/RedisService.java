package taobao.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
public class RedisService implements InitializingBean {

    RedisTemplate redisTemplate;

    Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static int cap = 1 << 26; // 8m;

    private static int seeds [] = {3, 5 ,7, 9, 15, 31, 63, 127};

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

    public void multiSetBits (String key, boolean value, long ...offsets) {
        logger.info("key -> {}, value -> {}, offsets -> {}", key, value, offsets);
        byte[] keyByte = key.getBytes();
        redisTemplate.executePipelined((RedisCallback<?>) connection->{
            for (long offset : offsets) {
                connection.setBit(keyByte, offset, value);
            }
            return null;
        });
    }

    public List<Boolean> multiGetBits(String key, long ... offsets) {
        byte[] keyByte = key.getBytes();
        List<Boolean> lists = Lists.newArrayList();
        List<Object> results = redisTemplate.executePipelined((RedisCallback<?>)connection -> {
            for (long offset: offsets) {
                connection.getBit(keyByte, offset);
            }
            return null;
        });

        results.forEach(result -> {
            lists.add((Boolean) result);
        });

        return lists;
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

    private static int hash(String key, int seed) {
        int hash = 0;
        int length = key.length();
        for (int i=0; i<length; i++) {
            hash = hash * seed + key.charAt(i);
        }
        return (cap - 1) & hash;
    }


    private static List<Integer> hashs (String key) {
        List<Integer> list = Lists.newArrayList();
        for (int seed : seeds) {
            list.add(hash(key, seed));
        }
        return list;
    }

    private static long[] bloomFilterOffsets (String value) {
        List<Integer> hashs = hashs(value);
        Set<Long> sets = Sets.newHashSet();
        hashs.stream().forEach(i->sets.add((long)i));
        long offsets[] = new long[hashs.size()];
        int i = 0;
        for (long offset: sets) {
            offsets[i++] = offset;
        }
        return offsets;
    }


    public void putByBloomFilter (String key, String value) {
        multiSetBits(key, Boolean.TRUE, bloomFilterOffsets(value));
    }

    public Boolean mightContainsByBloomFilter (String key, String value) {
        List<Boolean> booleans = multiGetBits(key, bloomFilterOffsets(value));
        for (Boolean b: booleans) {
            if (Boolean.FALSE.equals(b)) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
