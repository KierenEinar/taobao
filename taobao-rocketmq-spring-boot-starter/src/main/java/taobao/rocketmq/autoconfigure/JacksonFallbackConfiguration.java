package taobao.rocketmq.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(ObjectMapper.class)
public class JacksonFallbackConfiguration {

    @Bean
    public ObjectMapper objectMapper () {
        return new ObjectMapper();
    }

}
