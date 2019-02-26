package taobao.localmq.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import taobao.localmq.core.DefaultLocalMQTemplate;
import taobao.localmq.core.LocalMQTemplate;

@Configuration
@Import(LocalMQListenerContainerConfigure.class)
public class LocalMQConfigure {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.initialize();
        return executor;
    }

    @Bean
    public LocalMQTemplate localMQTemplate () {
        return new DefaultLocalMQTemplate();
    }




}
