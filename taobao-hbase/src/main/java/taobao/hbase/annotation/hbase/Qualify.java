package taobao.hbase.annotation.hbase;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Qualify {

    interface Type {
        String mulit = "mulit";
        String single = "single";
    }

    String value() default "";

    String type()  default Type.single;

    String prefix() default "";
}