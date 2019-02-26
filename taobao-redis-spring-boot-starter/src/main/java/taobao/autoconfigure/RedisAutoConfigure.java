package taobao.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import taobao.core.RedisService;

@Configuration
@ConditionalOnProperty(prefix = "redis", havingValue = "true", name = "enabled")
@ConditionalOnClass({RedisTemplate.class})
@Import(RedisService.class)
@EnableRedisRepositories
public class RedisAutoConfigure {

}
