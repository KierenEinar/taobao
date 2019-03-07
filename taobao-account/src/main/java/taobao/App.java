package taobao;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableApolloConfig
@EnableEurekaClient
@MapperScan("taobao.account.mapper")
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
