package taobao.hbase.data;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import taobao.hbase.annotation.hbase.FamilyColumn;
import taobao.hbase.annotation.hbase.HbaseEntity;
import taobao.hbase.annotation.hbase.Qualify;
import taobao.hbase.model.HModel;
import taobao.hbase.model.HbaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractHbaseRepository<T extends HbaseModel> {

    protected String getTableName (T t) {
        Assert.isTrue(t.getClass().isAnnotationPresent(HbaseEntity.class), "model should annotate with HbaseEntity");
        HbaseEntity hbaseEntity = t.getClass().getAnnotation(HbaseEntity.class);
        return hbaseEntity.table();
    }


    protected List<Put> getPuts (T t) {

        List<HModel> modelList = getModels(t);
        Assert.isTrue(!CollectionUtils.isEmpty(modelList), "at least one condition list");
        List<Put> puts = Lists.newArrayList();

        for (HModel hModel : modelList) {
            Put put = new Put(Bytes.toBytes(hModel.getRowkey()));
            put.addColumn(Bytes.toBytes(hModel.getFamilyColumn()),
                          Bytes.toBytes(hModel.getQualify()),
                          Bytes.toBytes(hModel.getValue()));
            puts.add(put);
        }

        return puts;

    }


    protected List<HModel> getModels (T t) {

        Assert.isTrue(StringUtils.isNotBlank(t.getRowkey()), "row key must not null");

        List<HModel> hModels = new ArrayList<>();

        for (Class clazz = t.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {

            Field fields[] = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(Boolean.TRUE);
                if ( field.getAnnotation(FamilyColumn.class) == null  || field.getAnnotation(Qualify.class) == null) continue;


                String familyColumn = field.getAnnotation(FamilyColumn.class).value();
                String qualify = field.getAnnotation(Qualify.class).value();
                String type = field.getAnnotation(Qualify.class).type();
                String prefix = field.getAnnotation(Qualify.class).prefix();
                Object value = null;
                try {
                    value = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (Objects.isNull(value)) continue;

                if (Qualify.Type.mulit.equalsIgnoreCase(type)) {

                    Assert.isTrue(StringUtils.isNotBlank(prefix), "column prefix must not null");
                    Assert.isTrue(value instanceof List , "field must implements List");

                    List list = (List)value;
                    if (CollectionUtils.isEmpty(list)) continue;

                    for (Object o : list) {

                        String uuid = UUID.randomUUID().toString().replaceAll("-","");

                        if (!prefix.endsWith("-")) {
                            uuid = "-" + uuid;
                        }

                        qualify = prefix + uuid;
                        HModel hModel = new HModel();
                        hModel.setRowkey(t.getRowkey());
                        hModel.setFamilyColumn(familyColumn);
                        hModel.setQualify(qualify);
                        hModel.setValue(o.toString());
                        hModels.add(hModel);

                    }

                } else {
                    HModel hModel = new HModel();
                    hModel.setRowkey(t.getRowkey());
                    hModel.setFamilyColumn(familyColumn);
                    hModel.setQualify(qualify);
                    hModel.setValue(value.toString());
                    hModels.add(hModel);
                }
            }

        }
        return hModels;
    }

}
