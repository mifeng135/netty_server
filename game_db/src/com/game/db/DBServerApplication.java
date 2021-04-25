package com.game.db;


import com.game.db.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;

import java.util.Arrays;

import static com.game.db.constant.GameConstant.SQL_KEY_GAME;
import static com.game.db.constant.GameConstant.SQL_KEY_LOGIN;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {


        SqlDaoConfig dbSqlConfig = new SqlDaoConfig();
        dbSqlConfig.setMasterFileName("master-db.properties");
        dbSqlConfig.setPreSqlName("pre-sql.sqls");
        dbSqlConfig.getSlaveFileList().add("lmaster-slave.properties");

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login.properties");

        SqlDao.getInstance().initWithConfigList(dbSqlConfig, loginSqlConfig);


        new PropertiesConfig();
        CtrlAnnotation.getInstance().init(DBServerApplication.class.getPackage().getName());
        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount);
        RedisCache.getInstance();
        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
    }
}
