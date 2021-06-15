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

    public static String redisLoginIp;
    public static String redisLoginPassword;
    public static int redisLoginThreadCount;
    public static int redisLoginNettyThreadCount;
    public static int redisLoginDB;


    public static String dbServerIp;
    public static int dbServerPort;

    public static int logicDBSocketIndex;


    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    private void initData() {
        serverId = ConfigUtil.getInt("game_server_id_100");
        loginServerId = ConfigUtil.getInt("login_server_id");

        redisIp = ConfigUtil.getString("db_redis_ip");
        redisPassword = ConfigUtil.getString("db_redis_password");
        redisThreadCount = ConfigUtil.getInt("db_redis_thread_count");
        redisNettyThreadCount = ConfigUtil.getInt("db_redis_netty_thread_count");
        redisDB = ConfigUtil.getInt("db_redis_db");


        redisLoginIp = ConfigUtil.getString("login_redis_ip");
        redisLoginPassword = ConfigUtil.getString("login_redis_password");
        redisLoginThreadCount = ConfigUtil.getInt("login_redis_thread_count");
        redisLoginNettyThreadCount = ConfigUtil.getInt("login_redis_netty_thread_count");
        redisLoginDB = ConfigUtil.getInt("login_redis_db");


        dbServerIp = ConfigUtil.getString("db_server_ip");
        dbServerPort = ConfigUtil.getInt("db_server_port");

        logicDBSocketIndex = ConfigUtil.getInt("logic_db_socket_index");
    }
}
