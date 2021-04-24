package com.game.db;


import com.game.db.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {
        new PropertiesConfig();
        initAnnotation();
        initSql();
        initRedis();
        initHttpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
    }

    public static void initAnnotation() {
        CtrlAnnotation.getInstance().init(DBServerApplication.class.getPackage().getName());
    }

    private static void initSql() {

    }

    private static void initRedis() {
        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount);
        RedisCache.getInstance();
    }

    private static void initHttpServer() {
        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);
    }
}
