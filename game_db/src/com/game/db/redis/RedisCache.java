package com.game.db.redis;


import bean.player.PlayerInfoBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import core.redis.RedisDao;
import core.util.ConfigUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;


import static com.game.db.constant.RedisConstant.*;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RMapCache<Integer, PlayerInfoBean> playerCache; // key account
    private RMapCache<Integer, PlayerSceneBean> sceneCache;
    private RMapCache<Integer, PlayerRoleBean> roleCache;

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadLoginMap();
        loadSceneMap();
        loadRoleMap();
    }

    private void loadLoginMap() {
        int maxCount = ConfigUtil.getInt("redis_max_online_player_count", 1);
        RedissonClient redissonClient = RedisDao.getInstance().getRedisSon();
        playerCache = redissonClient.getMapCache(REDIS_ACCOUNT_LOGIN_KEY);
        playerCache.setMaxSize(maxCount);
    }

    private void loadSceneMap() {
        int maxCount = ConfigUtil.getInt("redis_max_online_player_count", 1);
        RedissonClient redissonClient = RedisDao.getInstance().getRedisSon();
        sceneCache = redissonClient.getMapCache(REDIS_SCENE_KEY);
        sceneCache.setMaxSize(maxCount);
    }

    private void loadRoleMap() {
        int maxCount = ConfigUtil.getInt("redis_max_online_player_count", 1);
        RedissonClient redissonClient = RedisDao.getInstance().getRedisSon();
        roleCache = redissonClient.getMapCache(REDIS_PLAYER_ROLE_KEY);
        roleCache.setMaxSize(maxCount);
    }

    public RMapCache<Integer, PlayerRoleBean> getRoleCache() {
        return roleCache;
    }

    public RMapCache<Integer, PlayerInfoBean> getPlayerCache() {
        return playerCache;
    }

    public RMapCache<Integer, PlayerSceneBean> getSceneCache() {
        return sceneCache;
    }
}
