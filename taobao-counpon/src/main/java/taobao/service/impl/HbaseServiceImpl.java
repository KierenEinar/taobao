package taobao.service.impl;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;
import taobao.model.HModel;
import taobao.service.HbaseService;

import java.io.IOException;

@Service
public class HbaseServiceImpl implements HbaseService {

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Autowired
    Connection connection;

    public String get(HModel hModel) {
        return hbaseTemplate.get(hModel.getTableName(),
                hModel.getRowKey(),
                hModel.getFamilyColumn(),
                hModel.getQualify(),
                new RowMapper<String>() {
                    public String mapRow(Result result, int i){
                        return Bytes.toString(result.value());
                    }
                });
    }

    public Table hTable (String tableName) throws IOException {
       return connection.getTable(TableName.valueOf(tableName));
    }

    public Boolean checkAndPut (HModel hModel, String oldValue, String newValue) throws IOException {
        Table table = hTable(hModel.getTableName());

        Put put = new Put(Bytes.toBytes(newValue));

        return table.checkAndPut(Bytes.toBytes(hModel.getRowKey()),
                Bytes.toBytes(hModel.getFamilyColumn()),
                Bytes.toBytes(hModel.getQualify()),
                Bytes.toBytes(oldValue),
                put);
    }


}
