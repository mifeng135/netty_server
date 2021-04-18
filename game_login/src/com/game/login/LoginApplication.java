package com.game.login;


import bean.login.LoginPlayerBean;
import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;

import java.io.IOException;

import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {

        //SqlDao.getInstance().init(SQL_MASTER, "dao.properties");


//        long t1 = System.currentTimeMillis();
//        LoginPlayerBean playerBean = SqlDao.getInstance().getDao(SQL_MASTER).fetch(LoginPlayerBean.class, "3fd");
//        long t2 = System.currentTimeMillis();
//        System.out.println(t2 - t1);


        new ProperticeConfig();
        SqlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        initSql();
        initRedis();
        initHttpServer();
        CtrlAnnotation.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }

    private static void initRedis() {
        RedisManager.getInstance().init(ProperticeConfig.redisIp, ProperticeConfig.redisPassword,
                ProperticeConfig.redisThreadCount, ProperticeConfig.redisNettyThreadCount);
        RedisCache.getInstance();
    }

    private static void initSql() {
        SqlAnnotation.getInstance().initSql(SQL_MASTER, ProperticeConfig.loginServerId, "login-master.xml");
    }

    private static void initHttpServer() {
        new HttpServer(ProperticeConfig.httpIp, ProperticeConfig.httpPort);
    }
}
