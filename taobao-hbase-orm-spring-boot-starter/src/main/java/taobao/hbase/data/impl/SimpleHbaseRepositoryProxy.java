package taobao.hbase.data.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import taobao.hbase.data.AbstractHbaseRepository;
import taobao.hbase.data.HbaseRepository;
import taobao.hbase.data.SimpleHbaseRespository;
import taobao.hbase.model.HModel;
import taobao.hbase.model.HbaseModel;

import java.io.IOException;
import java.util.List;

public class SimpleHbaseRepositoryProxy<T extends HbaseModel> extends AbstractHbaseRepository<T> implements HbaseRepository<T>, SimpleHbaseRespository{

    private HbaseTemplate hbaseTemplate;

    private Connection connection;

    public HbaseTemplate getHbaseTemplate() {
        return hbaseTemplate;
    }

    public void setHbaseTemplate(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public SimpleHbaseRepositoryProxy (HbaseTemplate hbaseTemplate, Connection connection) {
        this.hbaseTemplate = hbaseTemplate;
        this.connection = connection;
    }

    public Boolean upsert(T t) {
        List<Put> puts = super.getPuts(t);
        Table table = null;
        try {
            table = hTable(t);
            table.put(puts);
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    public T findOne(String rowkey, Class<T> tClass) {

        return hbaseTemplate.get(getTableName(tClass), rowkey, ((result, i) -> {
            List<Cell> cells = result.listCells();
            if (CollectionUtils.isEmpty(cells)) return null;
            T model = newInstance(tClass);
            cells.stream().forEach(cell -> {
                super.cloneValueIntoModelByHbaseCell(cell, model);
            });
            return model;
        }));
    }


    public String get(HModel hModel) {
        return hbaseTemplate.get(hModel.getTableName(),
                hModel.getRowkey(),
                hModel.getFamilyColumn(),
                hModel.getQualify(),
                (Result result, int i)-> Bytes.toString(result.value()));
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
