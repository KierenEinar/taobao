package taobao.hbase.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import taobao.hbase.service.HbaseService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
@AutoConfigureAfter({HbaseTemplate.class, Connection.class})
public class HbaseServiceImpl implements HbaseService {

    HbaseTemplate hbaseTemplate;

    Connection connection;

    public HbaseServiceImpl (HbaseTemplate hbaseTemplate, Connection connection ) {
       this.hbaseTemplate = hbaseTemplate;
       this.connection = connection;
    }


    @Override
    public void put(String table, String rowKey, String columnFamily, String qualify, byte[] value) {
        hbaseTemplate.put(table, rowKey, columnFamily, qualify, value);
    }

    @Override
    public void put(String tableName, String rowKey, List<Put> putList) {
        Table table = getTable(tableName);
        try {
            table.put(putList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(String table, String rowKey, String columnFamily, String qualify, Class<T> clazz) {
        return hbaseTemplate.get(table, rowKey, columnFamily, qualify, (Result result, int i) -> JSONObject.parseObject(new String(result.getRow()), clazz));
    }

    @Override
    public Map<String, String> get(String table, String rowKey, String columnFamily) {
        return hbaseTemplate.get(table, rowKey, columnFamily, (Result result, int i) -> {
            List<Cell> cells = result.listCells();
            Map<String, String> map = new ConcurrentHashMap<>();
            for (Cell cell : cells) {
                String qualify = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                map.put(qualify, value);
            }
            return map;
        });
    }

    @Override
    public Map<String, Map<String, String>> get(String table, String rowKey) {
        return hbaseTemplate.get(table, rowKey, (Result result, int i) -> {
            List<Cell> cells = result.listCells();
            Map<String, Map<String, String>> map = new ConcurrentHashMap<>();
            for (Cell cell : cells) {
                String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                String qualify = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                if (!map.containsKey(family)) {
                    map.put(family, new ConcurrentHashMap<>());
                }
                Map<String, String> m = map.get(family);
                m.put(qualify, value);
            }
            return map;
        });
    }

    @Override
    public Boolean checkAndPut(String tableName, String rowKey, String columnFamily, String qualify, String oldValue, String newValue) {
        Table table = getTable(tableName);
        Put put = new Put(tableName.getBytes());
        put.addColumn(columnFamily.getBytes(), qualify.getBytes(), newValue.getBytes());
        Boolean result = Boolean.FALSE;
        try {
            result = table.checkAndPut(rowKey.getBytes(), columnFamily.getBytes(), qualify.getBytes(), oldValue.getBytes(), put);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Table getTable(String tableName) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return table;
    }

}
