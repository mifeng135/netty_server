package core.annotation.redis;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Redis {

    String name() default "";

    String IncrName() default "";

    String dbName() default "";

    boolean immediately() default false;

    int incrId() default 0;
}
