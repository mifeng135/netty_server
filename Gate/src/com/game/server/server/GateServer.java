package com.game.server.server;


import com.game.server.ServerConfig;


/**
 * Created by Administrator on 2020/6/8.
 */
public class GateServer {

    public void start() {

//        startSendSocket(ServerConfig.SEND_SERVER_LIST);
//        startReceiveSocket(ServerConfig.RECEIVE_SERVER_LIST);

//        new GateWebSocket(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT);
//        new EventThreadGroup(ServerConfig.REGION_GAME, GameEventHandler.class, 1);


        new GateTcpServer(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT);
    }
}
