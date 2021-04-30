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

        redisIp = ConfigUtil.getString("redis_ip");
        redisPassword = ConfigUtil.getString("redis_password");
        redisThreadCount = ConfigUtil.getInt("redis_thread_count");
        redisNettyThreadCount = ConfigUtil.getInt("redis_netty_thread_count");
        db = ConfigUtil.getInt("redis_db");

        httpIp = ConfigUtil.getString("http_ip");
        httpPort = ConfigUtil.getInt("http_port");

        redisPlayerCacheCount = ConfigUtil.getInt("redis_player_cache_count");
    }
}