package taobao.rocketmq.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.MQAdmin;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import taobao.rocketmq.config.RocketMQTransactionAnnotationProcessor;
import taobao.rocketmq.config.TransactionHandlerRegistry;
import taobao.rocketmq.core.DefaultRocketMQTemplate;
import taobao.rocketmq.core.RocketMQTemplate;


@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass({MQAdmin.class, ObjectMapper.class})
@ConditionalOnProperty(prefix = "rocketmq", havingValue = "true", name = "enabled")
@Import({JacksonFallbackConfiguration.class, RocketMQListenerContainerConfiguration.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class RocketMQAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DefaultMQProducer.class)
    DefaultMQProducer defaultMQProducer (RocketMQProperties rocketMQProperties) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();

        Assert.hasText(rocketMQProperties.getNameServer(), "rocketmq namesrv not null");
        Assert.hasText(rocketMQProperties.getProducer().getGroup(), "rocketmq producer group not null");

        defaultMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        defaultMQProducer.setProducerGroup(rocketMQProperties.getProducer().getGroup());
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(rocketMQProperties.getProducer().getRetryTimesWhenSendAsyncFailed());
        defaultMQProducer.setCompressMsgBodyOverHowmuch(rocketMQProperties.getProducer().getCompressMessageBodyThreshold());
        defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQProperties.getProducer().getRetryTimesWhenSendFailed());
        defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(rocketMQProperties.getProducer().isRetryNextServer());
        return defaultMQProducer;
    }

    @Bean
    @ConditionalOnClass({DefaultMQProducer.class, ObjectMapper.class})
    RocketMQTemplate rocketMQTemplate (DefaultMQProducer producer, ObjectMapper objectMapper) {
        RocketMQTemplate defaultRocketMQTemplate = new DefaultRocketMQTemplate();
        defaultRocketMQTemplate.setProducer(producer);
        defaultRocketMQTemplate.setObjectMapper(objectMapper);
        return defaultRocketMQTemplate;
    }

    @Bean
    @ConditionalOnClass(RocketMQTemplate.class)
    TransactionHandlerRegistry transactionHandlerRegistry (RocketMQTemplate rocketMQTemplate) {
        return new TransactionHandlerRegistry(rocketMQTemplate);
    }

    @Bean
    @ConditionalOnClass(TransactionHandlerRegistry.class)
    RocketMQTransactionAnnotationProcessor rocketMQTransactionAnnotationProcessor (TransactionHandlerRegistry transactionHandlerRegistry) {
        return new RocketMQTransactionAnnotationProcessor(transactionHandlerRegistry);
    }
}
