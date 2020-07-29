package com.game.server.serverConfig;

import com.game.server.adapter.SynAdapter;
import com.game.server.core.config.ServerInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/5/28.
 */
public class ServerConfig {

    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PASSWORD = "nqwl0520";


    public static final String SERVER_PACKAGE = "com.game.server";


    public static final String HTTP_SERVER_IP = "192.168.1.217";
    public static final int HTTP_PORT = 8000;


    public static final String REGION_LOGIN = "login";
    public static final String REGION_SYN = "syn";


    public static final byte GATE_LOGIN_SERVER_KEY = 2;

    public static final List<ServerInfo> SEND_SERVER_LIST = Arrays.asList(

    );

    public static final List<ServerInfo> RECEIVE_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 7000, GATE_LOGIN_SERVER_KEY, "gate_login",new SynAdapter())
    );
}
