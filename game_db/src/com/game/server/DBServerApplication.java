package com.game.server;


import com.fasterxml.jackson.databind.util.LRUMap;
import com.game.server.bean.PlayerBean;
import com.game.server.bean.PlayerScene;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.netty.tcp.TcpServer;
import core.redis.RedisManager;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.cache.LRUCacheMap;


import java.util.concurrent.TimeUnit;

import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;
import static config.Config.*;
import static core.Constants.LOCAL;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        RedisCache.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
        new HttpServer(DB_HTTP_SERVER_IP, DB_HTTP_SERVER_PORT);
    }
}
