package taobao.hbase.model;

import java.io.Serializable;

public class HModel extends HbaseModel implements Serializable {

    private String tableName;
    private String familyColumn;
    private String qualify;
    private String value;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFamilyColumn() {
        return familyColumn;
    }

    public void setFamilyColumn(String familyColumn) {
        this.familyColumn = familyColumn;
    }

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
