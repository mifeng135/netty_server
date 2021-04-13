package com.game.server;


import com.game.server.redis.RedisCache;
import config.Config;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.util.ConfigUtil;

import static config.Config.*;
import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) throws Exception {
        ConfigUtil.loadFile("config.properties");
        initSql();
        initRedis();
        initHttpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }

    private static void initRedis() {
        String redisIp = ConfigUtil.getString("redis_ip");
        String redisPassword = ConfigUtil.getString("redis_password");
        int redisThreadCount = ConfigUtil.getInt("redis_thread_count", 1);
        int redisNettyThreadCount = ConfigUtil.getInt("redis_netty_thread_count", 1);
        RedisManager.getInstance().init(redisIp, redisPassword, redisThreadCount, redisNettyThreadCount);
        RedisCache.getInstance();
    }

    private static void initSql() {
        int loginServerId = ConfigUtil.getInt("login_server_id", 1);
        SqlAnnotation.getInstance().initSql(SQL_MASTER, loginServerId, "login-master.xml");

        int serverId100 = ConfigUtil.getInt("db_server_id_100", 100);
        SqlAnnotation.getInstance().initSql(SQL_MASTER, serverId100, "login-master.xml");
    }

    private static void initHttpServer() {
        String httpIp = ConfigUtil.getString("http_ip");
        int port = ConfigUtil.getInt("http_port", 8000);
        new HttpServer(httpIp, port);
    }
}
