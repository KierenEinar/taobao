package taobao.hbase.data;

import taobao.hbase.model.HbaseModel;

import java.io.IOException;

public interface HbaseRepository<T extends HbaseModel> {
    Boolean upsert (T t) throws IOException;
    T findOne (T t) throws IOException;
}
