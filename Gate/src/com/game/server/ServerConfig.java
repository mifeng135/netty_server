package com.game.server;

import com.game.server.adapter.DBAdapter;
import com.game.server.adapter.GameAdapter;
import core.Configs;
import core.SocketInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ServerConfig {

    public static final int SERVER_PORT = 7005;
    public static final String SERVER_IP = "127.0.0.1";

    public static final String REGION_DB = "DB";
    public static final String REGION_GAME = "GAME";


    public static final List<SocketInfo> SOCKET_INFO = Arrays.asList(
            new SocketInfo("127.0.0.1", 6000, "gate_game", Configs.SOCKET_TYPE_CLIENT, new GameAdapter()),
            new SocketInfo("127.0.0.1", 6001, "gate_db", Configs.SOCKET_TYPE_CLIENT, new DBAdapter())
    );
}
