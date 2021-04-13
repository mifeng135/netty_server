package com.game.server;


import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.util.ConfigUtil;

import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_SLAVE;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {
        ConfigUtil.loadFile("db-config.properties");
        initSql();
        initRedis();
        initHttpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
    }

    private static void initSql() {
        int serverId = ConfigUtil.getInt("server_id", 1);
        SqlAnnotation.getInstance().initSql(SQL_MASTER, serverId, "db-master.xml");
        SqlAnnotation.getInstance().initSql(SQL_SLAVE, serverId, "db-slave.xml");
    }

    private static void initRedis() {
        String redisIp = ConfigUtil.getString("redis_ip");
        String redisPassword = ConfigUtil.getString("redis_password");
        int redisThreadCount = ConfigUtil.getInt("redis_thread_count", 1);
        int redisNettyThreadCount = ConfigUtil.getInt("redis_netty_thread_count", 1);
        RedisManager.getInstance().init(redisIp, redisPassword, redisThreadCount, redisNettyThreadCount);
        RedisCache.getInstance();
    }

    private static void initHttpServer() {
        String httpIp = ConfigUtil.getString("db_http_ip");
        int port = ConfigUtil.getInt("db_http_port", 8001);
        new HttpServer(httpIp, port);
    }
}
