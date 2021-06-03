package com.game.login;

import bean.login.LoginNoticeBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerSceneBean;
import com.game.login.redis.RedisCache;
import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import core.annotation.CtrlA;
import core.annotation.QueryA;
import core.group.EventThreadGroup;
import core.netty.http.HttpHandler;
import core.netty.http.HttpServer;
import core.redis.MFReplicator;
import core.redis.RedisManager;
import core.sql.SqlBinLog;
import core.sql.MysqlBinLog;
import core.sql.SqlDao;
import core.sql.SqlDaoConfig;
import core.util.*;
import org.apache.log4j.PropertyConfigurator;
import org.nutz.dao.Cnd;

import java.io.IOException;

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
        QueryA.getInstance().init(Util.getPackageName(LoginApplication.class));

        new PropertiesConfig("config.properties");

        //new MysqlBinLog("127.0.0.1", 3306, "root", "123456").registerEvent(new SqlBinLog()).start();

        RedisManager.getInstance().init(PropertiesConfig.redisIp, PropertiesConfig.redisPassword,
                PropertiesConfig.redisThreadCount, PropertiesConfig.redisNettyThreadCount, PropertiesConfig.db);
        //RedisCache.getInstance();

        new HttpServer(PropertiesConfig.httpIp, PropertiesConfig.httpPort, new HttpHandler(HTTP_DECODER_TYPE_JSON));
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginEventHandler.class, LoginApplication.class.getName());


        new MFReplicator("", 0, "").start();


        Thread.sleep(1000);
        Instance.redis().mapPut("1", 2, new PlayerItemBean());
        Instance.redis().mapPut("1", 3, new PlayerSceneBean());

        PlayerItemBean playerItemBean = Instance.redis().mapGet("1", 2);


//        LoginNoticeBean bean =  new LoginNoticeBean();
//        bean.setNoticeId(1);
//        bean.setContent("2121");
//        Instance.sql().update(bean);

    }
}
