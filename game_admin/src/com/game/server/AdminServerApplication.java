package com.game.server;


import com.game.server.redis.RedisCache;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;

import static config.Config.*;

/**
 * Created by Administrator on 2020/6/1.
 */
public class AdminServerApplication {

    public static void main(String[] args) {
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        RedisCache.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, AdminServerApplication.class.getName());
        new HttpServer(DB_HTTP_SERVER_IP, DB_HTTP_SERVER_PORT);
    }
}
