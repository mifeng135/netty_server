package com.game.login;

import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;

import java.util.Arrays;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        SqlDao.getInstance().init("login-master-dao.properties",
                "pre-sql.sqls",
                Arrays.asList("login-master-slave.properties"));

        new ProperticeConfig();
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        RedisManager.getInstance().init(ProperticeConfig.redisIp, ProperticeConfig.redisPassword,
                ProperticeConfig.redisThreadCount, ProperticeConfig.redisNettyThreadCount);
        RedisCache.getInstance();
        new HttpServer(ProperticeConfig.httpIp, ProperticeConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }
}
