package taobao.rocketmq.annotation;

public enum MessageModel {

    CLUSTERING("CLUSTERING"),
    BROADCASTING("BROADCASTING");

    private final String modeCN;

    public String getModeCN() {
        return modeCN;
    }

    MessageModel (String modeCN) {this.modeCN = modeCN;}

}
