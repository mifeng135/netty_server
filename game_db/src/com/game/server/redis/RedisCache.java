package com.game.server.redis;


import com.game.server.bean.PlayerBean;
import com.game.server.bean.PlayerScene;
import core.annotation.SqlAnnotation;
import core.redis.RedisManager;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.game.server.DBConfig.*;
import static com.game.server.constant.RedisConstant.*;
import static com.game.server.constant.SqlCmdConstant.SERVER_LIST_SELECT_ALL;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return RedisCache.DefaultInstance.INSTANCE;
    }

    private RMapCache<String, PlayerBean> accountLoginCache; // key account
    private RMapCache<Integer, PlayerScene> sceneCache;

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadLoginMap();
        loadSceneMap();
    }

    private void loadLoginMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        accountLoginCache = redissonClient.getMapCache(REDIS_ACCOUNT_LOGIN_KEY);
        accountLoginCache.setMaxSize(REDIS_MAX_ONLINE_COUNT);
    }

    private void loadSceneMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        sceneCache = redissonClient.getMapCache(REDIS_SCENE_KEY);
        sceneCache.setMaxSize(REDIS_MAX_ONLINE_COUNT);
    }

    public RMapCache<String, PlayerBean> getAccountLoginCache() {
        return accountLoginCache;
    }

    public RMapCache<Integer, PlayerScene> getSceneCache() {
        return sceneCache;
    }
}
