package com.game.login;

import bean.login.LoginNoticeBean;
import com.game.login.redis.RedisCache;
import core.annotation.CtrlA;
import core.annotation.QueryA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import java.util.Collection;
import java.util.List;

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

        CtrlA.getInstance().init(LoginApplication.class.getPackage().getName(), new LoginExceptionHandler());
        QueryA.getInstance().init(LoginApplication.class.getPackage().getName());


        new PropertiesConfig("config.properties");

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());

        RedisManager.getInstance().scoreSetAdd("eee", 200, 1);
        RedisManager.getInstance().scoreSetAdd("eee", 12, 2);
        RedisManager.getInstance().scoreSetAdd("eee", 1, 3);

        Collection mm = RedisManager.getInstance().scoreSetGet("eee", 1);

        int rank = RedisManager.getInstance().scoreSetRank("eee", 2);


        int index = RedisManager.getInstance().getNextIndex();
    }
}
