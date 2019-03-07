package taobao.rocketmq.constant;

public enum DelayTimeLevel {

    DelayTimeLevel_1s(1),
    DelayTimeLevel_5s(2),
    DelayTimeLevel_10s(3),
    DelayTimeLevel_30s(4),
    DelayTimeLevel_1m(5),
    DelayTimeLevel_2m(6),
    DelayTimeLevel_3m(7),
    DelayTimeLevel_4m(8),
    DelayTimeLevel_5m(9),
    DelayTimeLevel_6m(10),
    DelayTimeLevel_7m(11),
    DelayTimeLevel_8m(12),
    DelayTimeLevel_9m(13),
    DelayTimeLevel_10m(14),
    DelayTimeLevel_20m(15),
    DelayTimeLevel_30m(16),
    DelayTimeLevel_1h(17),
    DelayTimeLevel_2h(18);

    private int level;

    DelayTimeLevel (int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
