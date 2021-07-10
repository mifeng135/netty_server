package core.annotation.proto;

import java.lang.annotation.*;

@Deprecated
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proto {
    String protoFile() default "";

    String[] importFile() default "";
}
