package core.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Redis {

    String name() default "";

    String IncrName() default "";

    String dbName() default "";

    boolean immediately() default false;
}
