package core.redis;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RedisConfig {
    private String masterStr;
    private List<String> slaveStr;
    private String pwd;
    private int thread;
    private int nettyThread;
    private int db;
    private String key = "default";
}
