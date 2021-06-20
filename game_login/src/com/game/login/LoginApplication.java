package com.game.login;

import core.annotation.ctrl.CtrlA;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.annotation.proto.ParseProtoFile;
import core.redis.RedisConfig;
import core.redis.RedisDao;
import core.sql.*;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {
    public static void main(String[] args) {

        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        CtrlA.getInstance().init(LoginApplication.class, new LoginExceptionHandler());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());
        initDao();
        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);

    }

    public static void initDao() {
        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        new PropertiesConfig("config.properties");

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setDb(PropertiesConfig.db);
        redisConfig.setMasterStr(PropertiesConfig.redisIp);
        redisConfig.setPwd(PropertiesConfig.redisPassword);
        redisConfig.setThread(PropertiesConfig.redisThreadCount);
        redisConfig.setNettyThread(PropertiesConfig.redisNettyThreadCount);

        RedisDao.getInstance().init("bean.login", redisConfig);
    }
}
