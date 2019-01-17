package taobao.rocketmq.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import taobao.rocketmq.support.RocketMQHeaders;

import java.nio.charset.Charset;
import java.util.Objects;

public class DefaultRocketMQTemplate extends AbstractMessageSendingTemplate<String> implements InitializingBean, DisposableBean, RocketMQTemplate {

    Logger logger = LoggerFactory.getLogger(DefaultRocketMQTemplate.class);

    private DefaultMQProducer producer;

    private ObjectMapper objectMapper;

    private String charset = "UTF-8";

    private MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void setProducer(DefaultMQProducer defaultMQProducer) {
        this.producer = defaultMQProducer;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SendResult syncSend(String destination, Object playload, long timeout) {
        Message<?> message = this.doConvert(playload, null ,null);
        return syncSend(destination, message, timeout);
    }

    @Override
    public SendResult syncSend(String destination, Object playload) {
        return syncSend(destination, playload, producer.getSendMsgTimeout());
    }

    @Override
    public SendResult syncSend(String destination, Message<?> message, long timeout) {
        if (Objects.isNull(destination) || Objects.isNull(message.getPayload())) {
            throw new IllegalArgumentException("destination and payload not null");
        }

        org.apache.rocketmq.common.message.Message msg = this.convertToRocketMessage(
                objectMapper,
                charset,
                destination,
                message);

        try {
            return producer.send(msg, timeout);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException(e.getMessage(), e);
        }
    }

    @Override
    public SendResult syncSend(String destination, Message<?> message) {
        return syncSend(destination, message, producer.getSendMsgTimeout());
    }

    @Override
    public SendResult syncSendOrderly(String destination, Message<?> message, String hashKey) {
        return syncSendOrderly(destination, message, hashKey, producer.getSendMsgTimeout());
    }

    @Override
    public SendResult syncSendOrderly(String destination, Message<?> message, String hashKey, long timeout) {
        if (Objects.isNull(destination) || Objects.isNull(message.getPayload())) {
            throw new IllegalArgumentException("destination and payload not null");
        }

        org.apache.rocketmq.common.message.Message msg = this.convertToRocketMessage(objectMapper,
                charset, destination, message);

        try {
            return producer.send(msg, messageQueueSelector, hashKey, timeout);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException(e.getMessage(), e);
        }
    }

    @Override
    public SendResult syncSendOrderly(String destination, Object payload, String hashKey, long timeout) {
        Message<?> message = this.doConvert(payload, null, null);
        return syncSendOrderly(destination, message, hashKey,timeout );
    }

    @Override
    public SendResult syncSendOrderly(String destination, Object payload, String hashKey) {
        return syncSendOrderly(destination, payload, hashKey, producer.getSendMsgTimeout());
    }

    @Override
    public void sendAsync(String destination, Object playload, SendCallback sendCallback, long timeout) {
        Message<?> message = this.doConvert(playload, null, null);
        sendAsync(destination, message, sendCallback, timeout);
    }

    @Override
    public void sendAsync(String destination, Object playload, SendCallback sendCallback) {
        Message<?> message = this.doConvert(playload, null, null);
        sendAsync(destination, message, sendCallback, producer.getSendMsgTimeout());
    }

    @Override
    public void sendAsync(String destination, Message<?> message, SendCallback sendCallback, long timeout) {

        if (Objects.isNull(destination) || Objects.isNull(message)) {
            throw new IllegalStateException("destination and payload not null");
        }

        org.apache.rocketmq.common.message.Message msg = this.convertToRocketMessage(objectMapper
        ,charset, destination, message);

        try {
            producer.send(msg, sendCallback);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("asyncSend failed d -> {}, msg -> {}", destination, message);
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    @Override
    public void sendAsync(String destination, Message<?> message, SendCallback sendCallback) {
        sendAsync(destination, message, sendCallback, producer.getSendMsgTimeout());
    }

    @Override
    public void sendAsyncOrderly(String destination, Message<?> message, String hashKey, SendCallback sendCallback) {
        sendAsyncOrderly(destination, message, hashKey, sendCallback, producer.getSendMsgTimeout());
    }

    @Override
    public void sendAsyncOrderly(String destination, Message<?> message, String hashKey, SendCallback sendCallback, long timeout) {
        if (Objects.isNull(destination) || Objects.isNull(message)) {
            throw new IllegalStateException("destination and payload not null");
        }
        org.apache.rocketmq.common.message.Message msg = this.convertToRocketMessage(objectMapper,
                charset, destination, message);
        try {
            producer.send(msg, messageQueueSelector, hashKey, sendCallback, timeout);
        } catch (Exception e) {
            logger.error("asyncSend orderly failed d -> {}, msg -> {}", destination, message);
            throw new IllegalStateException(e.getMessage(), e);

        }

    }

    @Override
    public void sendAsyncOrderly(String destination, Object payload, String hashKey, SendCallback sendCallback, long timeout) {
        Message<?> message = this.doConvert(payload, null, null);
        sendAsyncOrderly(destination, message, hashKey, sendCallback, timeout);
    }

    @Override
    public void sendAsyncOrderly(String destination, Object payload, String hashKey, SendCallback sendCallback) {
        sendAsyncOrderly(destination, payload, hashKey, sendCallback, producer.getSendMsgTimeout());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.nonNull(producer)) {
            producer.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(producer)) {
            producer.shutdown();
        }
    }



    @Override
    protected void doSend(String s, Message<?> message) {

    }

    @Override
    public Message convertToSpringMessage(MessageExt message) {
        org.springframework.messaging.Message retMessage =
                MessageBuilder.withPayload(message.getBody()).
                        setHeader(RocketMQHeaders.KEYS, message.getKeys()).
                        setHeader(RocketMQHeaders.TAGS, message.getTags()).
                        setHeader(RocketMQHeaders.TOPIC, message.getTopic()).
                        setHeader(RocketMQHeaders.MESSAGE_ID, message.getMsgId()).
                        setHeader(RocketMQHeaders.BORN_TIMESTAMP, message.getBornTimestamp()).
                        setHeader(RocketMQHeaders.BORN_HOST, message.getBornHostString()).
                        setHeader(RocketMQHeaders.FLAG, message.getFlag()).
                        setHeader(RocketMQHeaders.QUEUE_ID, message.getQueueId()).
                        setHeader(RocketMQHeaders.SYS_FLAG, message.getSysFlag()).
                        setHeader(RocketMQHeaders.TRANSACTION_ID, message.getTransactionId()).
                        setHeader(RocketMQHeaders.PROPERTIES, message.getProperties()).
                        build();

        return retMessage;
    }

    @Override
    public Message convertToSpringMessage(org.apache.rocketmq.common.message.Message message) {
        org.springframework.messaging.Message retMessage =
                MessageBuilder.withPayload(message.getBody()).
                        setHeader(RocketMQHeaders.KEYS, message.getKeys()).
                        setHeader(RocketMQHeaders.TAGS, message.getTags()).
                        setHeader(RocketMQHeaders.TOPIC, message.getTopic()).
                        setHeader(RocketMQHeaders.FLAG, message.getFlag()).
                        setHeader(RocketMQHeaders.TRANSACTION_ID, message.getTransactionId()).
                        setHeader(RocketMQHeaders.PROPERTIES, message.getProperties()).
                        build();

        return retMessage;
    }

    @Override
    public org.apache.rocketmq.common.message.Message convertToRocketMessage(ObjectMapper objectMapper, String charset, String destination, Message<?> message) {
        Object payloadObj = message.getPayload();
        byte[] payloads;

        if (payloadObj instanceof String) {
            payloads = ((String) payloadObj).getBytes(Charset.forName(charset));
        } else {
            try {
                String jsonObj = objectMapper.writeValueAsString(payloadObj);
                payloads = jsonObj.getBytes(Charset.forName(charset));
            } catch (Exception e) {
                throw new RuntimeException("convert to RocketMQ message failed.", e);
            }
        }

        String[] tempArr = destination.split(":", 2);
        String topic = tempArr[0];
        String tags = "";
        if (tempArr.length > 1) {
            tags = tempArr[1];
        }

        org.apache.rocketmq.common.message.Message rocketMsg = new org.apache.rocketmq.common.message.Message(topic, tags, payloads);

        MessageHeaders headers = message.getHeaders();
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            Object keys = headers.get(RocketMQHeaders.KEYS);
            if (!StringUtils.isEmpty(keys)) { // if headers has 'KEYS', set rocketMQ message key
                rocketMsg.setKeys(keys.toString());
            }

            Object flagObj = headers.getOrDefault("FLAG", "0");
            int flag = 0;
            try {
                flag = Integer.parseInt(flagObj.toString());
            } catch (NumberFormatException e) {
                // Ignore it
                logger.info("flag must be integer, flagObj:{}", flagObj);
            }
            rocketMsg.setFlag(flag);

            Object waitStoreMsgOkObj = headers.getOrDefault("WAIT_STORE_MSG_OK", "true");
            boolean waitStoreMsgOK = Boolean.TRUE.equals(waitStoreMsgOkObj);
            rocketMsg.setWaitStoreMsgOK(waitStoreMsgOK);

            headers.entrySet().stream()
                    .filter(entry -> !Objects.equals(entry.getKey(), RocketMQHeaders.KEYS)
                            && !Objects.equals(entry.getKey(), "FLAG")
                            && !Objects.equals(entry.getKey(), "WAIT_STORE_MSG_OK")) // exclude "KEYS", "FLAG", "WAIT_STORE_MSG_OK"
                    .forEach(entry -> {
                        rocketMsg.putUserProperty("USERS_" + entry.getKey(), String.valueOf(entry.getValue())); // add other properties with prefix "USERS_"
                    });

        }

        return rocketMsg;
    }
}
