package com.game.db;


import core.annotation.ctrl.CtrlA;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;
import core.redis.RedisDao;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static com.game.db.constant.GameConstant.SQL_KEY_LOGIN;
import static core.Constants.LOCAL;


/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {

        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig dbSqlConfig = new SqlDaoConfig();
        dbSqlConfig.setMasterFileName("db/db-master-dao.properties");
        dbSqlConfig.setPreSqlName("pre-sql.sqls");

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("db/db-login-dao.properties");
        loginSqlConfig.setSqlKey(SQL_KEY_LOGIN);

        SqlDao.getInstance().initWithConfigList(dbSqlConfig, loginSqlConfig);

        new PropertiesConfig("config.properties");

        CtrlA.getInstance().init(DBServerApplication.class.getPackage().getName());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, DBServerApplication.class.getName());


        RedisDao.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.redisDB);

        new TcpServer(PropertiesConfig.dbServerIp, PropertiesConfig.dbServerPort, LOCAL).startServer();
    }
}
