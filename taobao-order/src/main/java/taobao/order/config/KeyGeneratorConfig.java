package taobao.order.config;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyGeneratorConfig {

    @Bean
    public KeyGenerator keyGenerator () {
        return new DefaultKeyGenerator();
    }

}
