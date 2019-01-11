package taobao.core.config.datasource;

//@Configuration
public class DatasourceConfiguration {


//    private Class<? extends DataSource> dataSourceType = org.apache.tomcat.jdbc.pool.DataSource.class;
//
//    Logger logger = LoggerFactory.getLogger(DatasourceConfiguration.class);
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.db1")
//    @Primary
//    public DataSource db1(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db2")
//    public DataSource db2(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db3")
//    public DataSource db3(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db4")
//    public DataSource db4(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db5")
//    public DataSource db5(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db6")
//    public DataSource db6(){
//        return DataSourceBuilder.create().type(dataSourceType).build();
//    }
//
//    @Bean
//    public Map<String, DataSource> dataSourceMap (@Qualifier("db1") DataSource db1,
//                                                  @Qualifier("db2") DataSource db2,
//                                                  @Qualifier("db3") DataSource db3,
//                                                  @Qualifier("db4") DataSource db4,
//                                                  @Qualifier("db5") DataSource db5,
//                                                  @Qualifier("db6") DataSource db6) {
//
//        HashMap<String, DataSource> dataSourceHashMap = new HashMap<String, DataSource>();
//        dataSourceHashMap.put("db1", db1);
//        dataSourceHashMap.put("db2", db2);
//        dataSourceHashMap.put("db3", db3);
//        dataSourceHashMap.put("db4", db4);
//        dataSourceHashMap.put("db5", db5);
//        dataSourceHashMap.put("db6", db6);
//        return dataSourceHashMap;
//    }
//
//
//    @Bean
//    public DataSource shardingDataSource (@Qualifier("dataSourceMap") Map<String, DataSource> dataSourceMap,
//                                          @Qualifier("userTableRuleConfiguration") TableRuleConfiguration userTableRuleConfiguration) throws SQLException {
//
//        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
//        shardingRuleConfiguration.getTableRuleConfigs().add(userTableRuleConfiguration);
//        Properties properties = new Properties();
//        properties.put("sql.show", Boolean.TRUE);
//        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, new ConcurrentHashMap(), properties);
//    }




}
