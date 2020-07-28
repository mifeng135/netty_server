package com.game.server.serverConfig;

import com.game.server.core.config.ServerInfo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ServerConfig {


    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PASSWORD = "nqwl0520";


    public static final String RECEIVE_FROM_GATE = "tcp://127.0.0.1:7001";
    public static final String SEND_TO_GATE = "tcp://127.0.0.1:6001";


    public static final List<ServerInfo> SEND_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 6001, 30)
    );

}
