package core.redis;

import core.util.ConfigUtil;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisManager {

    private static Config config = new Config();
    private static Redisson mRedisSon;

    private static class DefaultInstance {
        static final RedisManager INSTANCE = new RedisManager();
    }

    public static RedisManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RedisManager() {

    }

    public void init(String ip, String pwd) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        mRedisSon = (Redisson) Redisson.create(config);
    }

    public void init(String ip, String pwd, int thread, int nettyThread) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        config.setThreads(thread);
        config.setNettyThreads(nettyThread);
        mRedisSon = (Redisson) Redisson.create(config);
    }

    public Redisson getRedisSon() {
        return mRedisSon;
    }

}
