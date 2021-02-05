package com.game.server;


import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;

import static config.Config.*;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
        new HttpServer(HTTP_IP, HTTP_PORT);
    }
}
