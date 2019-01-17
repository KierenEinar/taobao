package taobao.rocketmq.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQMessageListener {

    String consumerGroup();

    String topic();

    SelectorType selectorType() default SelectorType.TAG;

    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    MessageModel messageModel() default MessageModel.CLUSTERING;

    String selectorExpression () default "*";
}
