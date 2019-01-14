package taobao.hbase.annotation.hbase;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HbaseEntity {
    String table () default "";
}
