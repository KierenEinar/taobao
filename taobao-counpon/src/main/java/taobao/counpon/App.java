package taobao.counpon;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableApolloConfig
@EnableEurekaClient
@ComponentScan("taobao")
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
