package core.annotation.redis;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RedisInfo {
    private Class cls;
    private String tableName;
    private String incrName;
    private String dbName;
    private boolean immediately;
}
