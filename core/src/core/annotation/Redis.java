package core.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Redis {

    enum IncrType {INT, STRING};

    String name() default "";

    String IncrName() default "";

    String dbName() default "";

    IncrType type() default IncrType.INT;

    boolean immediately() default false;
}
