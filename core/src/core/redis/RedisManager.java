package core.redis;

import core.Configs;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisManager {

    private Config config = new Config();
    private Redisson mRedisSon;
    private static class DefaultInstance {
        static final RedisManager INSTANCE = new RedisManager();
    }

    public static RedisManager getInstance() {
        return DefaultInstance.INSTANCE;
    }


    private RedisManager() {
        config.useSingleServer().setAddress(Configs.REDIS_IP);
        config.useSingleServer().setPassword(Configs.REDIS_PASSWORD);
        mRedisSon = (Redisson) Redisson.create(config);
    }

    public Redisson getRedisSon() {
        return mRedisSon;
    }

}
