package com.game.login;

import core.util.ConfigUtil;

public class PropertiesConfig {

    public static int loginServerId;


    public static String redisIp;
    public static String redisPassword;
    public static int redisThreadCount;
    public static int redisNettyThreadCount;
    public static int db;


    public static String httpIp;
    public static int httpPort;


    public static int redisPlayerCacheCount;


    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    private void initData() {
        loginServerId = ConfigUtil.getInt("login_server_id");

        redisIp = ConfigUtil.getString("login_redis_ip");
        redisPassword = ConfigUtil.getString("login_redis_password");
        redisThreadCount = ConfigUtil.getInt("login_redis_thread_count");
        redisNettyThreadCount = ConfigUtil.getInt("login_redis_netty_thread_count");
        db = ConfigUtil.getInt("login_redis_db");

        httpIp = ConfigUtil.getString("login_http_ip");
        httpPort = ConfigUtil.getInt("login_http_port");

        redisPlayerCacheCount = ConfigUtil.getInt("login_redis_player_cache_count");
    }
}
