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
import core.util.Util;
import org.apache.log4j.PropertyConfigurator;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;


import java.lang.reflect.Field;

import static core.Constants.HTTP_DECODER_TYPE_JSON;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {


        long current = System.currentTimeMillis();

        for (int index = 0; index < 10000000; index++) {
            Field[] f = LoginNoticeBean.class.getDeclaredFields();

            for (int i = 0; i < f.length; i++) {
                Field field = f[i];
                Id ii = field.getAnnotation(Id.class);
                String name = field.getName();
                int mm = 0;
            }
        }
        long last = System.currentTimeMillis();
        System.out.println(last - current);


        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        CtrlA.getInstance().init(Util.getPackageName(LoginApplication.class), new LoginExceptionHandler());
        QueryA.getInstance().init(Util.getPackageName(LoginApplication.class));


        new PropertiesConfig("config.properties");

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());
    }
}
