package core.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Redis {
    String name() default "";

    String key();

    int storeType() default 1;

    int maxStoreSize() default 1000;

}
