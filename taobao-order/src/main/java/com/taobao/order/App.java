package com.taobao.order;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableApolloConfig
@EnableEurekaClient
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
