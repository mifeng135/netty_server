package com.game.server.serverConfig;

import com.game.server.adapter.DBAdapter;
import com.game.server.adapter.GameAdapter;
import com.game.server.core.config.ServerInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ServerConfig {

    public static final int SERVER_PORT = 7005;
    public static final String SERVER_IP = "192.168.1.217";

    public static final byte GATE_GAME_SERVER_KEY = 1;
    public static final byte GATE_LOGIN_SERVER_KEY = 2;
    public static final byte GATE_DB_SERVER_KEY = 3;

    public static final List<ServerInfo> SEND_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 7000, GATE_LOGIN_SERVER_KEY, "gate_login"),
            new ServerInfo("tcp://127.0.0.1", 7001, GATE_GAME_SERVER_KEY, "gate_game"),
            new ServerInfo("tcp://127.0.0.1", 7002, GATE_DB_SERVER_KEY, "gate_db")
    );

    public static final List<ServerInfo> RECEIVE_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 6000, GATE_GAME_SERVER_KEY, "gate_game", new GameAdapter()),
            new ServerInfo("tcp://127.0.0.1", 6001, GATE_DB_SERVER_KEY, "gate_db", new DBAdapter())
    );

}
