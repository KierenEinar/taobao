package taobao.hbase.annotation.hbase;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface FamilyColumn {
    String value() default "";
}
