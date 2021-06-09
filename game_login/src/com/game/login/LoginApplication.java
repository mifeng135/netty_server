package com.game.login;

import bean.login.LoginNoticeBean;
import com.alibaba.fastjson.JSON;
import constants.RedisTableKey;
import core.annotation.CtrlA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisDao;
import core.sql.*;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static core.Constants.HTTP_DECODER_TYPE_JSON;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        CtrlA.getInstance().init(Util.getPackageName(LoginApplication.class), new LoginExceptionHandler());

        new PropertiesConfig("config.properties");

        RedisDao.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);


        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());

        Map<String, Integer> test = new ConcurrentHashMap<>();
        test.put("1", 1);
        String mmmm = JSON.toJSONString(test);
        Map<String, Integer> test1 = JSON.parseObject(mmmm, ConcurrentHashMap.class);


        LoginNoticeBean loginNoticeBean = new LoginNoticeBean();
        loginNoticeBean.setId(Ins.redis().getNextIncrement(RedisTableKey.GAME_NOTICE_LIST));
        loginNoticeBean.setContent("111111111");
        Ins.redis().put(RedisTableKey.GAME_NOTICE_LIST, loginNoticeBean);
        int mmm = 0;
    }
}
