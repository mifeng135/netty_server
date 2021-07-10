package core.annotation.redis;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Redis {

    String name() default ""; //表名

    String IncrName() default ""; //自增长mysql字段名称

    String dbName() default ""; //db名称

    boolean immediately() default false; // 是否立刻执行 更新数据库

    int incrId() default 0; //如果mysql有自增长名字字段 设置自增id才有作用

    boolean delete() default false; //玩家下线的时候是否从redis中删除缓存
}
