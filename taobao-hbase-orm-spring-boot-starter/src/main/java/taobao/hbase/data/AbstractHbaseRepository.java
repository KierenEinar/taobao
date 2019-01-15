package taobao.hbase.data;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import taobao.hbase.annotation.hbase.FamilyColumn;
import taobao.hbase.annotation.hbase.HbaseEntity;
import taobao.hbase.annotation.hbase.Qualify;
import taobao.hbase.model.HModel;
import taobao.hbase.model.HbaseModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class AbstractHbaseRepository<T extends HbaseModel> {

    protected T newInstance (Class<T> tClass) {
        try {
            return  tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


//    protected Class<T> getTClass () {
//        return (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    }


    protected String getTableName (Class<T> t) {
        HbaseEntity hbaseEntity = t.getAnnotation(HbaseEntity.class);
        return hbaseEntity.table();
    }


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

                        //String uuid = UUID.randomUUID().toString().replaceAll("-","");

                        String uuid = o.toString();

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


    protected void cloneValueIntoModelByHbaseCell (Cell cell, T model) {

        Map<String, Field> fieldMap = findFamilyQualifyField (model);
        String family = Bytes.toString(cell.getFamilyArray());
        String qualify = Bytes.toString(cell.getQualifierArray());
        byte value[] = cell.getValueArray();

        Field field = null;

        String fq = toColumnQualify(family, qualify);

        if (fieldMap.containsKey(fq)) {
            field = fieldMap.get(fq);
        } else {
            String k = fieldMap.keySet().stream().filter(i-> fq.contains(i)).findFirst().get();
            if (fieldMap.containsKey(k)) {
                field = fieldMap.get(k);
            }
        }

        if (Objects.nonNull(field)) cloneValueIntoModelByHbaseCell(field, value, model);

    }

    protected void cloneValueIntoModelByHbaseCell (Field field, byte[] value, T model) {

        Class clazz = field.getType();
        field.setAccessible(Boolean.TRUE);
        try {

            field.set(model, transferValue(clazz, value));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Object transferValue (Class type, byte[] value) {

        String typeName = type.getSimpleName();
        String methodName = "to" + typeName;
        try {
            Method method = Bytes.class.getMethod(methodName, byte[].class);
            return method.invoke(null, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected Map<String, Field> findFamilyQualifyField (T t) {

        Map<String, Field> fieldMap = new HashMap<>();
        Field fields[] =  t.getClass().getDeclaredFields();
        for (Field field : fields) {

           if (!field.isAnnotationPresent(FamilyColumn.class) && !field.isAnnotationPresent(Qualify.class))
               continue;

           String column = field.getAnnotation(FamilyColumn.class).value();
           String qualify = field.getAnnotation(Qualify.class).value();
           String prefix = field.getAnnotation(Qualify.class).prefix();
           String type = field.getAnnotation(Qualify.class).type();
           if (Qualify.Type.mulit.equalsIgnoreCase(type)) {

               Assert.isTrue(StringUtils.isNotBlank(prefix), "qualify type being multi, prefix must not null");
               qualify = prefix;

           }

           Assert.isTrue(StringUtils.isNotBlank(column),"family column must declare");
           fieldMap.put(toColumnQualify(column, qualify), field);

        }
        return fieldMap;
    }

    private String toColumnQualify (String column, String qualify) {
        return column + "&" + qualify;
    }




}
