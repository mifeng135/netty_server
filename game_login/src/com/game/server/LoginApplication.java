package com.game.server;


import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.netty.tcp.TcpConnection;
import core.redis.RedisManager;

import static config.Config.*;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
        new HttpServer(HTTP_IP, HTTP_PORT);
        new TcpConnection(LOGIN_DB_SOCKET_INDEX).connect(DB_SERVER_IP, DB_SERVER_PORT);
    }
}
