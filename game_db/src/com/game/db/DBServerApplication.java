package com.game.db;


import com.game.db.redis.RedisCache;
import core.annotation.CtrlAnnotation;
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
        new PropertiesConfig();
        initAnnotation();
        initSql();
        initRedis();
        initHttpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
    }

    public static void initAnnotation() {
        CtrlAnnotation.getInstance().init(DBServerApplication.class.getPackage().getName());
        SqlAnnotation.getInstance().init(DBServerApplication.class.getPackage().getName());
    }

    private static void initSql() {
        SqlAnnotation.getInstance().initSql(SQL_MASTER, PropertiesConfig.serverId, "db-master.xml");
        SqlAnnotation.getInstance().initSql(SQL_SLAVE, PropertiesConfig.serverId, "db-slave.xml");
        SqlAnnotation.getInstance().initSql(SQL_MASTER, PropertiesConfig.loginServerId, "login.xml");
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
