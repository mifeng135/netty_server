package com.game.server.serverConfig;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ServerConfig {

    public static final int SERVER_PORT = 7005;
    public static final String SERVER_IP = "192.168.1.217";
    public static final String SERVER_LOGIN_IP = "tcp://*:5000";


    public static final String RECEIVE_FROM_GATE_1 = "tcp://127.0.0.1:7000";
    public static final String SEND_TO_GATE_1 = "tcp://127.0.0.1:6000";



    public static final String RECEIVE_FROM_GAME = "tcp://127.0.0.1:6001";
    public static final String SEND_TO_GAME = "tcp://127.0.0.1:7001";
}
