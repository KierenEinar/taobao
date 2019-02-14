package taobao.product.config;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyGeneratorConfig {

    @Bean(name = "DefaultKeyGenerator")
    public io.shardingsphere.core.keygen.KeyGenerator defaultKeyGenerator () {
        return new DefaultKeyGenerator();
    }

}
