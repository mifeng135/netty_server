package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;
import core.redis.RedisManager;


import static config.Config.*;
import static core.Constants.LOCAL;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
        new TcpServer(DB_SERVER_IP, DB_SERVER_PORT, LOCAL).startServer();
    }
}
