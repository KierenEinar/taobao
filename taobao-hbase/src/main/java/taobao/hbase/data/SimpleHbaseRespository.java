package taobao.hbase.data;

import org.apache.hadoop.hbase.client.Table;
import taobao.hbase.model.HModel;
import taobao.hbase.model.HbaseModel;

import java.io.IOException;

public interface SimpleHbaseRespository {
    String get(HModel hModel);
    Boolean checkAndPut (HModel hModel, String oldValue, String newValue) throws IOException;
    Boolean put (HModel hModel);
}
