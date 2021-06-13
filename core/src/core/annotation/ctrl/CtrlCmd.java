package core.annotation.ctrl;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2020/5/28.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CtrlCmd {
    int cmd() default -1;
    String httpCmd() default "";
}
