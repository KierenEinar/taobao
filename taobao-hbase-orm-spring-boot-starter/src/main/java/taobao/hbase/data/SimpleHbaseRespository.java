package taobao.hbase.data;

import taobao.hbase.model.HModel;
import java.io.IOException;

public interface SimpleHbaseRespository {
    String get(HModel hModel);
    Boolean checkAndPut (HModel hModel, String oldValue, String newValue) throws IOException;
    Boolean put (HModel hModel);
}
