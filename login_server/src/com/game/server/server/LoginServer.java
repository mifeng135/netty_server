package com.game.server.server;

import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.netty.NettyServer;
import com.game.server.core.redis.RedisManager;
import com.game.server.core.sql.MysqlBatchHandle;
import com.game.server.eventGroup.LoginEventGroup;
import com.game.server.eventGroup.EventDispatch;
import com.game.server.eventGroup.LoginEventHandler;
import com.game.server.serverConfig.ServerConfig;
import com.game.server.socket.ReceiveFromGate;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginServer {

    public void start() {

        /**开启2条线程去处理登录*/
        new LoginEventGroup(LoginEventHandler.class, 2);
        EventDispatch.getInstance().start();

        /**启动redis mysql 批处理*/
        RedisManager.getInstance();
        MysqlBatchHandle.getInstance();

        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();


        /**开始gate连接数同步*/
        new ReceiveFromGate().start();

        /**开启http登录服务器*/
        NettyServer nettyServer = new NettyServer(ServerConfig.HTTP_SERVER_IP, ServerConfig.HTTP_PORT);
        nettyServer.doInitNetty();
    }

}
