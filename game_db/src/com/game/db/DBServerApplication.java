package com.game.db;


import com.game.db.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static com.game.db.constant.GameConstant.SQL_KEY_LOGIN;


/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {

        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig dbSqlConfig = new SqlDaoConfig();
        dbSqlConfig.setMasterFileName("db-master-dao.properties");
        dbSqlConfig.setPreSqlName("pre-sql.sqls");
        dbSqlConfig.getSlaveFileList().add("db-slave-dao.properties");

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("db-login-dao.properties");
        loginSqlConfig.setSqlKey(SQL_KEY_LOGIN);

        SqlDao.getInstance().initWithConfigList(dbSqlConfig, loginSqlConfig);

        new PropertiesConfig("db-config.properties");

        CtrlAnnotation.getInstance().init(DBServerApplication.class.getPackage().getName(), new DBExceptionHandler());
        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.redisDB);
        RedisCache.getInstance();
        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
    }
}
