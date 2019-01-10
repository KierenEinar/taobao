package taobao.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;

@Configuration
public class HbaseClientConfig {

    @Value("${hbase.zookeeper.servers}")
    String zookeepers;

    Logger logger = LoggerFactory.getLogger(HbaseClientConfig.class);

    @Bean("configuration")
    public org.apache.hadoop.conf.Configuration configuration () {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", zookeepers);
        return configuration;
    }


    @Bean
    public HbaseTemplate hbaseTemplate (@Qualifier("configuration") org.apache.hadoop.conf.Configuration configuration) {
        logger.info("哈哈哈 zookeepers -> {}", zookeepers);
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        hbaseTemplate.setConfiguration(configuration);
        return hbaseTemplate;
    }

    @Bean
    public Connection connection (@Qualifier("configuration") org.apache.hadoop.conf.Configuration configuration) throws IOException {
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }

}
