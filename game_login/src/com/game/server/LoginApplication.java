package com.game.server;


import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.DelayedQueue;
import core.redis.RedisManager;
import core.redis.TaskDelayEvent;
import core.util.TimeUtil;

import java.util.concurrent.TimeUnit;

import static com.game.server.Config.LOGIC_THREAD_NAME;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        RedisManager.getInstance().init(Config.REDIS_IP, Config.REDIS_PWD, Config.REDIS_THREAD_COUNT, Config.REDIS_NETTY_THREAD_COUNT);
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT, LOGIC_THREAD_NAME);
        new HttpServer(Config.HTTP_IP, Config.HTTP_PORT);
    }
}
