package taobao.user;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("taobao.user.mapper")
@EnableTransactionManagement
@EnableEurekaClient
@EnableApolloConfig
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
