package taobao.hbase.model;

public abstract class HbaseModel {

    private String rowkey;

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }
}
