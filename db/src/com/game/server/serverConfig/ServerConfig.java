package com.game.server.serverConfig;

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

    public static final String SEND_TO_GATE_1 = "tcp://*:7000";

    public static final String RECEIVE_FROM_GATE_1 = "tcp://127.0.0.1:6000";

    public static final byte GATE_DB_SERVER_KEY = 3;

    public static final List<ServerInfo> SEND_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 6001, GATE_DB_SERVER_KEY, "gate_db")
    );

    public static final List<ServerInfo> RECEIVE_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 7002, GATE_DB_SERVER_KEY, "gate_db")
    );

}
