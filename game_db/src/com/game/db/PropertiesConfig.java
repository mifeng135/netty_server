package com.game.db;

import core.util.ConfigUtil;

public class PropertiesConfig {

    public static int serverId;
    public static int loginServerId;

    public static String redisIp;
    public static String redisPassword;
    public static int redisThreadCount;
    public static int redisNettyThreadCount;
    public static int redisDB;

    public static String httpIp;
    public static int httpPort;

    public static int redisMaxCapacity;


    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    private void initData() {
        serverId = ConfigUtil.getInt("game_server_id_100");
        loginServerId = ConfigUtil.getInt("login_server_id");

        redisIp = ConfigUtil.getString("redis_ip");
        redisPassword = ConfigUtil.getString("redis_password");
        redisThreadCount = ConfigUtil.getInt("redis_thread_count");
        redisNettyThreadCount = ConfigUtil.getInt("redis_netty_thread_count");
        redisDB = ConfigUtil.getInt("redis_db");

        httpIp = ConfigUtil.getString("db_http_ip");
        httpPort = ConfigUtil.getInt("db_http_port");

        redisMaxCapacity = ConfigUtil.getInt("redis_max_online_player_count");
    }
}
