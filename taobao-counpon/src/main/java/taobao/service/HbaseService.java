package taobao.service;

import org.apache.hadoop.hbase.client.Table;
import taobao.model.HModel;

import java.io.IOException;

public interface HbaseService {
    String get(HModel hModel);
    Table hTable (String tableName) throws IOException;
    Boolean checkAndPut (HModel hModel, String oldValue, String newValue) throws IOException;
}
