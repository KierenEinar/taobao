package com.taobao.counpon.model;

import java.io.Serializable;

public class HModel implements Serializable {

    private String tableName;
    private String rowKey;
    private String familyColumn;
    private String qualify;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
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

    @Override
    public String toString() {
        return "HModel{" +
                "tableName='" + tableName + '\'' +
                ", rowKey='" + rowKey + '\'' +
                ", familyColumn='" + familyColumn + '\'' +
                ", qualify='" + qualify + '\'' +
                '}';
    }
}
