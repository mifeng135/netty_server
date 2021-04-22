package com.game.login;

import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;


import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        SqlDao.getInstance().init(SQL_MASTER, "dao.properties", "pre-sql.sqls");
        new ProperticeConfig();
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        RedisManager.getInstance().init(ProperticeConfig.redisIp, ProperticeConfig.redisPassword,
                ProperticeConfig.redisThreadCount, ProperticeConfig.redisNettyThreadCount);
        RedisCache.getInstance();
        new HttpServer(ProperticeConfig.httpIp, ProperticeConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }
}
