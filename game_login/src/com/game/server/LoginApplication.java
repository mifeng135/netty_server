package com.game.server;


import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;

import static config.Config.*;
import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        SqlAnnotation.getInstance().intiSqlWithKey(SQL_MASTER,"spring-mybatis-login-master.xml");
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        RedisCache.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
        new HttpServer(HTTP_IP, HTTP_PORT);
    }
}
