package com.taobao.core.config.shardingconfig.database;

//@Configuration
public class DataBaseShardingConfiguration {

//    @Bean
//    public TableRuleConfiguration userTableRuleConfiguration () {
//        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
//        tableRuleConfiguration.setLogicTable("users");
//        tableRuleConfiguration.setActualDataNodes("db${1..6}.users");
//        tableRuleConfiguration.setKeyGeneratorColumnName("id");
//        tableRuleConfiguration.setKeyGenerator(new DefaultKeyGenerator());
//        //tableRuleConfiguration.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", ));
//        tableRuleConfiguration.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "db${id % 6 + 1}"));
//        return tableRuleConfiguration;
//    }

}
