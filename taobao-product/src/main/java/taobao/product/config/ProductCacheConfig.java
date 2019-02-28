package taobao.product.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ProductCacheConfig {

    @Bean
    public Cache caffeineCache () {
        Cache cache = Caffeine.newBuilder()
                .expireAfterWrite(5L, TimeUnit.SECONDS)
                .build();
        return cache;
    }

    @Bean
    public Cache stockCache () {
        Cache cache = Caffeine.newBuilder()
                .expireAfterWrite(5L, TimeUnit.SECONDS)
                .build();
        return cache;
    }



}
