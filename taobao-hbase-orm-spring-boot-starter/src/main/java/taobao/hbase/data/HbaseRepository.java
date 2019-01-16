package taobao.hbase.data;

import taobao.hbase.model.HbaseModel;

import java.io.IOException;

public interface HbaseRepository<T extends HbaseModel> {
    Boolean insert (T t) ;
    T findOne (String rowkey, Class<T> tClass);
}
