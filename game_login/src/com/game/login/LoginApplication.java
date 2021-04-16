package com.game.login;


import bean.player.PlayerServerInfoBean;
import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.util.ConfigUtil;

import java.util.List;
import java.util.Map;

import static com.game.login.constant.SqlCmdConstant.PLAYER_SERVER_INFO_SELECT_ALL;
import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        new ProperticeConfig();
        SqlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        initSql();
        initRedis();
        initHttpServer();
        CtrlAnnotation.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
    }

    private static void initRedis() {
        RedisManager.getInstance().init(ProperticeConfig.redisIp, ProperticeConfig.redisPassword,
                ProperticeConfig.redisThreadCount, ProperticeConfig.redisNettyThreadCount);
        RedisCache.getInstance();
    }

    private static void initSql() {
        SqlAnnotation.getInstance().initSql(SQL_MASTER, ProperticeConfig.loginServerId, "login-master.xml");
    }

    private static void initHttpServer() {
        new HttpServer(ProperticeConfig.httpIp, ProperticeConfig.httpPort);
    }
}
