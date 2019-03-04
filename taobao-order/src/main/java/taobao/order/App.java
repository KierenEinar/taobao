package taobao.order;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableApolloConfig
@EnableEurekaClient
@MapperScan("taobao.order.mapper")
@EnableFeignClients
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
