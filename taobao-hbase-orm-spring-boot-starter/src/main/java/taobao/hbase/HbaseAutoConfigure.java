package taobao.hbase;


import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import taobao.hbase.config.HbaseClientConfig;
import taobao.hbase.data.HbaseRepository;
import taobao.hbase.data.impl.SimpleHbaseRepositoryProxy;
import taobao.hbase.service.HbaseService;
import taobao.hbase.service.impl.HbaseServiceImpl;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(prefix = "hbase.orm", value = "enabled", havingValue = "true")
@ConditionalOnClass(HbaseRepository.class)
@Import(HbaseServiceImpl.class)
public class HbaseAutoConfigure {

    @Value("${hbase.zookeeper.servers}")
    String zookeepers;

    Logger logger = LoggerFactory.getLogger(HbaseClientConfig.class);

    @Bean("configuration")
    @ConditionalOnMissingBean
    public org.apache.hadoop.conf.Configuration configuration () {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zookeepers);
        return configuration;
    }


    @Bean("hbaseTemplate")
    @ConditionalOnMissingBean
    public HbaseTemplate hbaseTemplate (@Qualifier("configuration") org.apache.hadoop.conf.Configuration configuration) {
        logger.info("哈哈哈 zookeepers -> {}", zookeepers);
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        hbaseTemplate.setConfiguration(configuration);
        return hbaseTemplate;
    }

    @Bean("connection")
    @ConditionalOnMissingBean
    public Connection connection (@Qualifier("configuration") org.apache.hadoop.conf.Configuration configuration) throws IOException {
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }


    @Bean
    @ConditionalOnMissingBean
    public HbaseRepository hbaseRepository (@Qualifier("hbaseTemplate") HbaseTemplate hbaseTemplate,@Qualifier("connection") Connection connection) {
        logger.info("实例化 hbaseRepository, {}, {}", hbaseTemplate, connection);
        return new SimpleHbaseRepositoryProxy(hbaseTemplate, connection);
    }

}
