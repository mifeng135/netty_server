package core.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2020/5/28.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ctrl {
}
