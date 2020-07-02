package com.game.db.serverConfig;

/**
 * Created by Administrator on 2020/5/28.
 */
public class ServerConfig {

    public static final String REDIS_IP = "redis://127.0.0.1:6379";

    public static final String REDIS_PASSWORD = "nqwl0520";


    public static final String SERVER_PACKAGE = "com.game.db";

    public static final String SEND_TO_GATE_1 = "tcp://*:7000";

    public static final String RECEIVE_FROM_GATE_1 = "tcp://127.0.0.1:6000";
}
