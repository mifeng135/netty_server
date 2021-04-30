package com.game.login;

import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {



        String value = FileUtil.getConfigFileString("MapConfig.json");
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        //loginSqlConfig.getSlaveFileList().add("login-master-slave.properties");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        new PropertiesConfig("login-config.properties");
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName(), new LoginExceptionHandler());

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }
}
