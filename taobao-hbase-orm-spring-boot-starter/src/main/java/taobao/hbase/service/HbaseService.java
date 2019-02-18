package taobao.hbase.service;

import org.apache.hadoop.hbase.client.Put;

import java.util.List;
import java.util.Map;
public interface HbaseService {

    void put(String table ,String rowKey, String columnFamily, String qualify, byte []value);

    void put(String table, String rowKey, List<Put> putList);

    <T> T get(String table ,String rowKey, String columnFamily, String qualify, Class<T> clazz);

    Map<String, String> get(String table, String rowKey, String columnFamily);

    Map<String, Map<String, String>> get(String table, String rowKey);

    Boolean checkAndPut (String table, String rowKey, String columnFamily, String qualify, String oldValue, String newValue);

}
