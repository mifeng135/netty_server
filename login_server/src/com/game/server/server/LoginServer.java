package com.game.server.server;

import com.game.server.adapter.SynAdapter;
import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.config.ServerInfo;
import com.game.server.core.manager.ReceiveSocketManager;
import com.game.server.core.manager.SendSocketManager;
import com.game.server.core.netty.NettyServer;
import com.game.server.core.redis.RedisManager;
import com.game.server.core.sql.MysqlBatchHandle;
import com.game.server.core.zero.Receive;
import com.game.server.core.zero.Send;
import com.game.server.eventGroup.login.LoginEventGroup;
import com.game.server.eventGroup.login.EventDispatch;
import com.game.server.eventGroup.login.LoginEventHandler;
import com.game.server.eventGroup.syn.SynEventGroup;
import com.game.server.eventGroup.syn.SynEventHandler;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginServer {

    public void start() {

        for (int i = 0; i < ServerConfig.SEND_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.SEND_SERVER_LIST.get(i);
            Send sendSocket = new Send(serverInfo.getIp(), serverInfo.getPort());
            sendSocket.start();
            SendSocketManager.getInstance().putSocket(serverInfo.getServerKey(), sendSocket);
        }

        for (int i = 0; i < ServerConfig.RECEIVE_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.RECEIVE_SERVER_LIST.get(i);
            Receive receiveSocket = new Receive(serverInfo.getIp(), serverInfo.getPort(), serverInfo.getServerKey(), new SynAdapter());
            receiveSocket.start();
            ReceiveSocketManager.getInstance().putSocket(serverInfo.getServerKey(), receiveSocket);
        }

        /**开启2条线程去处理登录*/
        new LoginEventGroup(LoginEventHandler.class, 2);
        EventDispatch.getInstance().start();

        /**开启1条线程处理gate同步消息*/
        new SynEventGroup(SynEventHandler.class, 1);

        /**启动redis mysql 批处理*/
        RedisManager.getInstance();
        MysqlBatchHandle.getInstance();

        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();

        /**开启http登录服务器*/
        NettyServer nettyServer = new NettyServer(ServerConfig.HTTP_SERVER_IP, ServerConfig.HTTP_PORT);
        nettyServer.doInitNetty();
    }

}
