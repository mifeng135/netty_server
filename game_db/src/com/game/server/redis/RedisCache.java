package com.game.server.redis;


import bean.player.PlayerBean;
import bean.player.PlayerScene;
import core.redis.RedisManager;
import core.util.ConfigUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;


import static com.game.server.constant.RedisConstant.*;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return RedisCache.DefaultInstance.INSTANCE;
    }

    private RMapCache<Integer, PlayerBean> playerCache; // key account
    private RMapCache<Integer, PlayerScene> sceneCache;

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadLoginMap();
        loadSceneMap();
    }

    private void loadLoginMap() {
        int maxCount = ConfigUtil.getInt("redis_max_online_player_count", 1);
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        playerCache = redissonClient.getMapCache(REDIS_ACCOUNT_LOGIN_KEY);
        playerCache.setMaxSize(maxCount);
    }

    private void loadSceneMap() {
        int maxCount = ConfigUtil.getInt("redis_max_online_player_count", 1);
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        sceneCache = redissonClient.getMapCache(REDIS_SCENE_KEY);
        sceneCache.setMaxSize(maxCount);
    }

    public RMapCache<Integer, PlayerBean> getPlayerCache() {
        return playerCache;
    }

    public RMapCache<Integer, PlayerScene> getSceneCache() {
        return sceneCache;
    }
}
