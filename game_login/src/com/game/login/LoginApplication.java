package com.game.login;

import bean.login.LoginNoticeBean;
import bean.player.PlayerItemBean;
import com.game.login.query.ServerListQuery;
import com.game.login.redis.RedisCache;
import core.annotation.CtrlA;
import core.annotation.QueryA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.MysqlBinLog;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;
import org.redisson.api.RSet;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static constants.SqlConstant.*;
import static core.Constants.HTTP_DECODER_TYPE_JSON;


/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException, IllegalAccessException {


        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));

        SqlDaoConfig loginSqlConfig = new SqlDaoConfig();
        loginSqlConfig.setMasterFileName("login/login-master-dao.properties");
        loginSqlConfig.setPreSqlName("pre-sql.sqls");
        loginSqlConfig.setDaoInterceptor(new LoginDaoInterceptor());
        SqlDao.getInstance().initWithConfigList(loginSqlConfig);

        CtrlA.getInstance().init(Util.getPackageName(LoginApplication.class), new LoginExceptionHandler());
        QueryA.getInstance().init(Util.getPackageName(LoginApplication.class));

        new PropertiesConfig("config.properties");

        new MysqlBinLog("127.0.0.1", 3306, "root", "123456").registerEvent(new LoginSqlBinLog()).start();
        Instance.redisTable();


        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());

        long t1 = System.currentTimeMillis();

        TestMap testMap = new TestMap();
        Map<Integer, PlayerItemBean> test = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            PlayerItemBean playerItemBean = new PlayerItemBean();
            playerItemBean.setPlayerIndex(1);
            playerItemBean.setItemId(i);
            playerItemBean.setItemCount(i);
            test.put(i, playerItemBean);
        }
        testMap.setTest(test);


        byte[] data = ProtoUtil.serialize(testMap);
        ProtoUtil.deserializer(data,TestMap.class);

        long t2 = System.currentTimeMillis();

        System.out.println(t2 - t1);

        Instance.query().invokeQuery(NOTICE_TEST1, 1);
    }
}
