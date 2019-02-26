package taobao.localmq.annotation;


import org.springframework.stereotype.Component;
import taobao.localmq.support.Constant;

import java.lang.annotation.*;

@Documented
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalMQMessageListener {
    String  topic() default Constant.DEFAULT_TOPIC;
    int capacity() default Constant.DEFAULT_CAPACITY;
    int maxPartition() default Constant.DEFAULT_MAX_PARTITION;
}
