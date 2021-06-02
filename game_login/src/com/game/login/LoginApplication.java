package com.game.login;

import com.game.login.redis.RedisCache;
import core.annotation.CtrlA;
import core.annotation.QueryA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlBinLog;
import core.sql.MysqlBinLog;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;

import static core.Constants.HTTP_DECODER_TYPE_JSON;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        CtrlA.getInstance().init(Util.getPackageName(LoginApplication.class), new LoginExceptionHandler());
        QueryA.getInstance().init(Util.getPackageName(LoginApplication.class));

        new PropertiesConfig("config.properties");

        new MysqlBinLog("127.0.0.1", 3306, "root", "111111").registerEvent(new SqlBinLog()).start();

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());
    }
}
