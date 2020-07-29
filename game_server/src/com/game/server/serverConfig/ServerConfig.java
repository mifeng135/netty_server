package com.game.server.serverConfig;

import com.game.server.adapter.GameAdapter;
import com.game.server.core.config.ServerInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ServerConfig {


    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PASSWORD = "nqwl0520";

    public static final byte GATE_GAME_SERVER_KEY = 1;

    public static final List<ServerInfo> SEND_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 6000, GATE_GAME_SERVER_KEY, "gate_game")
    );

    public static final List<ServerInfo> RECEIVE_SERVER_LIST = Arrays.asList(
            new ServerInfo("tcp://127.0.0.1", 7001, GATE_GAME_SERVER_KEY, "gate_game", new GameAdapter())
    );

}
