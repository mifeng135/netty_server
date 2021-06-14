package com.game.login;

import core.annotation.ctrl.CtrlA;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.annotation.proto.ParseProtoFile;
import core.redis.RedisDao;
import core.sql.*;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {
    public static void main(String[] args) {

        ParseProtoFile.createProtoFile();
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


    }
}
