package core.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.codec.FstCodec;
import org.redisson.config.Config;

/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisManager {

    private static Config config = new Config();
    private static RedissonClient redissonClient;

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
        config.useSingleServer().setDatabase(2);
        redissonClient = Redisson.create(config);

    }

    public void init(String ip, String pwd, int thread, int nettyThread,int db) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        config.useSingleServer().setDatabase(db);
        config.setThreads(thread);
        config.setNettyThreads(nettyThread);
        config.setCodec(new FstCodec());
        redissonClient = Redisson.create(config);
    }

    public RedissonClient getRedisSon() {
        return redissonClient;
    }
}
