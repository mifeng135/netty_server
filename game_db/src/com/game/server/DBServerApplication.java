package com.game.server;


import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;

import static config.Config.*;
import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_SLAVE;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {

        SqlAnnotation.getInstance().intiSqlWithKey(SQL_MASTER, "db-master.xml");
        SqlAnnotation.getInstance().intiSqlWithKey(SQL_SLAVE, "db-slave.xml");

        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        RedisCache.getInstance();

        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
        new HttpServer(DB_HTTP_SERVER_IP, DB_HTTP_SERVER_PORT);
    }
}
