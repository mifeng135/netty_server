package core.redis;

import org.redisson.api.LocalCachedMapOptions;

import java.util.concurrent.TimeUnit;

public class RedisOptions {

    public static LocalCachedMapOptions initLocalCachedMapOptions() {
        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LRU)
                .cacheSize(1)
                .maxIdle(1, TimeUnit.SECONDS)
                .timeToLive(1, TimeUnit.SECONDS)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);
        return options;
    }
}
