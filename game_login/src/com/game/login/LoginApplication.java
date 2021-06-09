package com.game.login;

import core.annotation.CtrlA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisDao;
import core.sql.*;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.Constants.HTTP_DECODER_TYPE_JSON;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {
    private static Logger logger = LoggerFactory.getLogger(LoginApplication.class);
    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        CtrlA.getInstance().init(LoginApplication.class, new LoginExceptionHandler());

        new PropertiesConfig("config.properties");

        RedisDao.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());

        SyncSql.getInstance().start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                SyncSql.getInstance().quit();
                logger.info("addShutdownHook finish");
            }
        }));
    }
}
