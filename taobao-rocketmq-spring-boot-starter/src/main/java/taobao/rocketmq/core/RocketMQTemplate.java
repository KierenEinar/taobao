package taobao.rocketmq.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;

public interface RocketMQTemplate extends InitializingBean, DisposableBean, MessageConverter {

    void setProducer (DefaultMQProducer defaultMQProducer);

    void setObjectMapper (ObjectMapper objectMapper);

    /**
     *
     * sync method
     * */
    SendResult syncSend (String destination, Object playload, long timeout);

    SendResult syncSend (String destination, Object playload);

    SendResult syncSend (String destination, Message<?> message, long timeout);

    SendResult syncSend (String destination, Message<?> message);

    SendResult syncSendOrderly (String destination, Message<?> message, String hashKey);

    SendResult syncSendOrderly (String destination, Message<?> message, String hashKey, long timeout);

    SendResult syncSendOrderly (String destination, Object payload, String hashKey, long timeout);

    SendResult syncSendOrderly (String destination, Object payload, String hashKey);

    /**
     *
     * async method
     * */


    void sendAsync (String destination, Object playload, SendCallback sendCallback, long timeout);

    void sendAsync (String destination, Object playload, SendCallback sendCallback);

    void sendAsync (String destination, Message<?> message, SendCallback sendCallback,  long timeout);

    void sendAsync (String destination, Message<?> message, SendCallback sendCallback);

    void sendAsyncOrderly (String destination, Message<?> message, String hashKey, SendCallback sendCallback);

    void sendAsyncOrderly (String destination, Message<?> message, String hashKey, SendCallback sendCallback, long timeout);

    void sendAsyncOrderly (String destination, Object payload, String hashKey, SendCallback sendCallback, long timeout);

    void sendAsyncOrderly (String destination, Object payload, String hashKey, SendCallback sendCallback);

}
