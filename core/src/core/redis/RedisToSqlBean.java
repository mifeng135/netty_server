package core.redis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisToSqlBean {
    private int type;
    private Object object;
}
