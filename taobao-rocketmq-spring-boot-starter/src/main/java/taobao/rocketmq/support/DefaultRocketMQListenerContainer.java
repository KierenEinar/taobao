package taobao.rocketmq.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import taobao.rocketmq.annotation.ConsumeMode;
import taobao.rocketmq.annotation.MessageModel;
import taobao.rocketmq.annotation.RocketMQMessageListener;
import taobao.rocketmq.annotation.SelectorType;
import taobao.rocketmq.core.RocketMQListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class DefaultRocketMQListenerContainer implements RocketMQListenerContainer {

    Logger logger = LoggerFactory.getLogger(DefaultRocketMQListenerContainer.class);

    private RocketMQMessageListener rocketMQMessageListener;

    private String nameSrv;

    private String consumerGroup;

    private String topic;

    private boolean isRunning;

    private RocketMQListener rocketMQListener;

    private ObjectMapper objectMapper;

    private String selectorExpression;

    private SelectorType selectorType;

    private ConsumeMode consumeMode;

    private MessageModel messageModel;

    private DefaultMQPushConsumer defaultMQPushConsumer;

    private Class messageType;

    private String charset = "UTF-8";

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setMessageType(Class messageType) {
        this.messageType = messageType;
    }

    public RocketMQMessageListener getRocketMQMessageListener() {
        return rocketMQMessageListener;
    }

    public void setRocketMQMessageListener(RocketMQMessageListener rocketMQMessageListener) {
        this.rocketMQMessageListener = rocketMQMessageListener;
//        this.consumerGroup = rocketMQMessageListener.consumerGroup();
//        this.topic = rocketMQMessageListener.topic();
        this.selectorExpression = rocketMQMessageListener.selectorExpression();
        this.selectorType = rocketMQMessageListener.selectorType();
        this.consumeMode = rocketMQMessageListener.consumeMode();
        this.messageModel = rocketMQMessageListener.messageModel();
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public SelectorType getSelectorType() {
        return selectorType;
    }

    public void setSelectorType(SelectorType selectorType) {
        this.selectorType = selectorType;
    }

    public ConsumeMode getConsumeMode() {
        return consumeMode;
    }


    public void setConsumeMode(ConsumeMode consumeMode) {
        this.consumeMode = consumeMode;
    }

    public String getSelectorExpression() {
        return selectorExpression;
    }

    public void setSelectorExpression(String selectorExpression) {
        this.selectorExpression = selectorExpression;
    }

    public String getNameSrv() {
        return nameSrv;
    }

    public void setNameSrv(String nameSrv) {
        this.nameSrv = nameSrv;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public RocketMQListener<?> getRocketMQListener() {
        return rocketMQListener;
    }

    public void setRocketMQListener(RocketMQListener<?> rocketMQListener) {
        this.rocketMQListener = rocketMQListener;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void setupRocketMQListener(RocketMQListener<?> rocketMQListener) {
        this.rocketMQListener = rocketMQListener;
    }

    @Override
    public void destroy() throws Exception {
        this.setRunning(Boolean.FALSE);
        if (Objects.nonNull(defaultMQPushConsumer)) {
            defaultMQPushConsumer.shutdown();
        }
        logger.info("container destroyed ..");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initRocketMQPushConsumer();
        this.messageType = getMessageType();
        logger.info("rocketmq messageType : {}", messageType.getName());
    }


    private void initRocketMQPushConsumer () throws MQClientException {

        defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setConsumerGroup(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(nameSrv);


        switch (messageModel) {

            case CLUSTERING:
                defaultMQPushConsumer.setMessageModel(org.apache.rocketmq.common.protocol.heartbeat.MessageModel.CLUSTERING);
                break;
            case BROADCASTING:
                defaultMQPushConsumer.setMessageModel(org.apache.rocketmq.common.protocol.heartbeat.MessageModel.BROADCASTING);
                break;
            default:
                throw new IllegalArgumentException("property messageModel is wrong");

        }


        switch (selectorType) {

            case TAG:
                defaultMQPushConsumer.subscribe(topic, selectorExpression);
                break;
            case SQL92:
                defaultMQPushConsumer.subscribe(topic,MessageSelector.bySql(selectorExpression));
                break;
            default:
                throw new IllegalArgumentException("property selectorType is wrong");
        }

        switch (consumeMode) {

            case ORDERLY:
                defaultMQPushConsumer.setMessageListener(new DefaultMessageListenerConcurrently());
                break;
            case CONCURRENTLY:
                defaultMQPushConsumer.setMessageListener(new DefaultMessageListenerOrderly());
                break;
            default:
                throw new IllegalArgumentException("property consumeMode is wrong");
        }

    }




    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    @Override
    public void start() {
        if (this.isRunning) {
            throw new IllegalStateException("container already running ..");
        }
        try {
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            throw new IllegalStateException("failed to start rocketmq push consumer", e);
        }
        this.setRunning(Boolean.TRUE);
        logger.info("running container ..");
    }

    @Override
    public void stop() {
        if (this.isRunning) {
            if (Objects.nonNull(defaultMQPushConsumer)) {
                defaultMQPushConsumer.shutdown();
            }
            setRunning(Boolean.FALSE);
        }
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


    public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : msgs) {
                try{
                    rocketMQListener.onMessage(doConvertMessage(messageExt));
                    logger.info("consume {}", messageExt.getMsgId());
                }catch (Exception e) {
                    logger.error("consume message failed. messageExt : {}", messageExt);
                    context.setDelayLevelWhenNextConsume(0);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            for (MessageExt messageExt : msgs) {
                try{
                    rocketMQListener.onMessage(doConvertMessage(messageExt));
                    logger.info("consume {}", messageExt.getMsgId());
                }catch (Exception e) {
                    logger.error("consume message failed. messageExt : {}", messageExt);
                    context.setSuspendCurrentQueueTimeMillis(1000);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
            return ConsumeOrderlyStatus.SUCCESS;
        }
    }



    private Object doConvertMessage (MessageExt messageExt) {

        if (Objects.equals(messageType, MessageExt.class)) return messageExt;

        String message = new String(messageExt.getBody(), Charset.forName(charset));

        if (Objects.equals(String.class, messageType)) {
            return message;
        }

        try {
            return objectMapper.readValue(message, messageType);
        } catch (IOException e) {
            logger.error("convert failed. str: {} , msgType : {}", message, messageType);
            throw new RuntimeException("convert failed. str: " + message + " ,msgType : " + messageType);
        }

    }

    private Class<?> getMessageType () {
        Class<?> targetClass = AopUtils.getTargetClass(rocketMQListener);
        Type interfaces[] = targetClass.getGenericInterfaces();
        Class<?> superClass = targetClass.getSuperclass();
        while ((Objects.isNull(interfaces) || interfaces.length == 0) &&  Objects.nonNull(superClass)) {
            interfaces = superClass.getGenericInterfaces();
            superClass = targetClass.getSuperclass();
        }
        if (Objects.nonNull(interfaces)) {

            for (Type type : interfaces) {

                if (type instanceof ParameterizedType) {

                    ParameterizedType parameterizedType = (ParameterizedType)type;

                    if (Objects.equals(parameterizedType.getRawType(), RocketMQListener.class)) {

                        Type[] acturalTypeArguments = parameterizedType.getActualTypeArguments();

                        if (Objects.nonNull(acturalTypeArguments) && acturalTypeArguments.length>0) {
                            return (Class<?>) acturalTypeArguments[0];
                        } else {
                            return Object.class;
                        }
                    }
                }

            }

            return Object.class;

        } else {
            return Object.class;
        }

    }

}
