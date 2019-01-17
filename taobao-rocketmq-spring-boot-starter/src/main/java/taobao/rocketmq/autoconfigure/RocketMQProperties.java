package taobao.rocketmq.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    private String nameServer;

    private Producer producer;

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public static class Producer {

        private String group;

        private int sendMessageTimeout = 3000;

        private int compressMessageBodyThreshold = 1024 * 4;

        private int retryTimesWhenSendAsyncFailed = 2;

        private int retryTimesWhenSendFailed = 2;

        private boolean retryNextServer = false;

        private int maxMessageSize = 1024 * 1024 * 4;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public int getSendMessageTimeout() {
            return sendMessageTimeout;
        }

        public void setSendMessageTimeout(int sendMessageTimeout) {
            this.sendMessageTimeout = sendMessageTimeout;
        }

        public int getCompressMessageBodyThreshold() {
            return compressMessageBodyThreshold;
        }

        public void setCompressMessageBodyThreshold(int compressMessageBodyThreshold) {
            this.compressMessageBodyThreshold = compressMessageBodyThreshold;
        }

        public int getRetryTimesWhenSendAsyncFailed() {
            return retryTimesWhenSendAsyncFailed;
        }

        public void setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
            this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
        }

        public boolean isRetryNextServer() {
            return retryNextServer;
        }

        public void setRetryNextServer(boolean retryNextServer) {
            this.retryNextServer = retryNextServer;
        }

        public int getMaxMessageSize() {
            return maxMessageSize;
        }

        public void setMaxMessageSize(int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
        }

        public int getRetryTimesWhenSendFailed() {
            return retryTimesWhenSendFailed;
        }

        public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
            this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        }
    }

}
