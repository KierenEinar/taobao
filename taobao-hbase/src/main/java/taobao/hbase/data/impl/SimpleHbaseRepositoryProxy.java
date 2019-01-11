package taobao.hbase.data.impl;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;
import taobao.hbase.data.AbstractHbaseRepository;
import taobao.hbase.data.HbaseRepository;
import taobao.hbase.data.SimpleHbaseRespository;
import taobao.hbase.model.HModel;
import taobao.hbase.model.HbaseModel;

import java.io.IOException;
import java.util.List;


@Service
public class SimpleHbaseRepositoryProxy<T extends HbaseModel> extends AbstractHbaseRepository<T> implements HbaseRepository<T>, SimpleHbaseRespository{

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Autowired
    Connection connection;


    public Boolean upsert(T t) throws IOException {
        List<Put> puts = super.getPuts(t);
        Table table = hTable(t);
        table.put(puts);
        return Boolean.TRUE;
    }


    public T findOne(T t) {
        return null;
    }


    public String get(HModel hModel) {
        return hbaseTemplate.get(hModel.getTableName(),
                hModel.getRowkey(),
                hModel.getFamilyColumn(),
                hModel.getQualify(),
                new RowMapper<String>() {
                    public String mapRow(Result result, int i){
                        return Bytes.toString(result.value());
                    }
                });
    }

    public Table hTable(T t) throws IOException {
        String tableName = super.getTableName(t);
        return connection.getTable(TableName.valueOf(tableName));
    }


    public Boolean checkAndPut (HModel hModel, String oldValue, String newValue) throws IOException {
        Table table = hTable((T)hModel);

        Put put = new Put(Bytes.toBytes(newValue));

        return table.checkAndPut(Bytes.toBytes(hModel.getRowkey()),
                Bytes.toBytes(hModel.getFamilyColumn()),
                Bytes.toBytes(hModel.getQualify()),
                Bytes.toBytes(oldValue),
                put);
    }

    public Boolean put(HModel hModel) {
        return null;
    }

}
