package com.game.server.redis;


import com.game.server.bean.ServerListBean;
import core.annotation.SqlAnnotation;
import core.redis.RedisManager;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.game.server.constant.RedisConstant.*;
import static com.game.server.constant.SqlCmdConstant.SERVER_LIST_SELECT_ALL;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RMap<Integer, ServerListBean> serverListCache;

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadServerListMap();
    }

    private void loadServerListMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        serverListCache = redissonClient.getMapCache(REDIS_SERVER_LIST_KEY);
        List<ServerListBean> allServerList = SqlAnnotation.getInstance().sqlSelectList(SERVER_LIST_SELECT_ALL);
        for (int i = 0; i < allServerList.size(); i++) {
            ServerListBean serverListBean = allServerList.get(i);
            serverListCache.put(serverListBean.getServerId(), serverListBean);
        }
    }

    public RMap<Integer, ServerListBean> getServerListCache() {
        return serverListCache;
    }
}
