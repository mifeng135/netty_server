package com.game.db;


import core.annotation.ctrl.CtrlA;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;
import core.redis.RedisConfig;
import core.redis.RedisDao;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static com.game.db.constant.GameConstant.KEY_LOGIN;
import static core.Constants.LOCAL;


/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {

        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        CtrlA.getInstance().init(DBServerApplication.class.getPackage().getName());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());
        initDao();
        new TcpServer(PropertiesConfig.dbServerIp, PropertiesConfig.dbServerPort, LOCAL).startServer();
    }

    public static void initDao() {
        SqlDaoConfig dbSqlConfig = new SqlDaoConfig();
        dbSqlConfig.setMasterFileName("db/db-master-dao.properties");
        dbSqlConfig.setPreSqlName("pre-sql.sqls");

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("db/db-login-dao.properties");
        loginSqlConfig.setSqlKey(KEY_LOGIN);

        SqlDao.getInstance().initWithConfigList(dbSqlConfig, loginSqlConfig);

        new PropertiesConfig("config.properties");

        RedisConfig redisDBConfig = new RedisConfig();
        redisDBConfig.setMasterStr(PropertiesConfig.redisIp);
        redisDBConfig.setPwd(PropertiesConfig.redisPassword);
        redisDBConfig.setThread(PropertiesConfig.redisThreadCount);
        redisDBConfig.setNettyThread(PropertiesConfig.redisNettyThreadCount);
        redisDBConfig.setDb(PropertiesConfig.redisDB);


        RedisConfig redisLoginConfig = new RedisConfig();
        redisLoginConfig.setMasterStr(PropertiesConfig.redisLoginIp);
        redisLoginConfig.setPwd(PropertiesConfig.redisLoginPassword);
        redisLoginConfig.setThread(PropertiesConfig.redisLoginThreadCount);
        redisLoginConfig.setNettyThread(PropertiesConfig.redisLoginNettyThreadCount);
        redisLoginConfig.setDb(PropertiesConfig.redisLoginDB);
        redisLoginConfig.setKey(KEY_LOGIN);


        RedisDao.getInstance().init(redisDBConfig, redisLoginConfig);
    }
}
