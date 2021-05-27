package com.game.login;

import bean.login.LoginNoticeBean;
import com.game.login.redis.RedisCache;
import core.annotation.CA;
import core.annotation.QA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

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
        //loginSqlConfig.getSlaveFileList().add("login/login-master-slave.properties");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);
        new PropertiesConfig("config.properties");
        CA.getInstance().init(LoginApplication.class.getPackage().getName(), new LoginExceptionHandler());
        QA.getInstance().init(LoginApplication.class.getPackage().getName());

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());

        List<LoginNoticeBean> mm = QA.getInstance().invokeQuery(1, 1, "1");

    }
}
