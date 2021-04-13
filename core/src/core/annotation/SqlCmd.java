package core.annotation;

import java.lang.annotation.*;

import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/13.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlCmd {
    short sqlCmd() default -1;
    short sqlType() default -1;
    short sqlKey() default SQL_MASTER; //默认为master
}
