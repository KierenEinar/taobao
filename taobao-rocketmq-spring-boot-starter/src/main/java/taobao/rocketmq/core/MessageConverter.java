package taobao.rocketmq.core;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface MessageConverter {

    org.springframework.messaging.Message convertToSpringMessage(
            org.apache.rocketmq.common.message.MessageExt message);

    org.springframework.messaging.Message convertToSpringMessage(
            org.apache.rocketmq.common.message.Message message);

    org.apache.rocketmq.common.message.Message convertToRocketMessage(
            ObjectMapper objectMapper, String charset,
            String destination, org.springframework.messaging.Message<?> message);


}
