package core.annotation;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RedisInfo {
    private Class cls;
    private String id;
    private String key;
    private int storeType;
}
