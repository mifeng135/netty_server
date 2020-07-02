package com.game.login.server;

import com.game.login.core.annotation.CtrlAnnotation;
import com.game.login.core.annotation.SqlAnnotation;
import com.game.login.core.groupHelper.MessageDispatchRegion;
import com.game.login.core.netty.NettyServer;
import com.game.login.core.redis.RedisManager;
import com.game.login.eventGroup.LoginEventGroup;
import com.game.login.eventGroup.EventDispatch;
import com.game.login.eventGroup.LoginEventHandler;
import com.game.login.serverConfig.ServerConfig;
import com.game.login.socket.ReceiveFromGate;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginServer {

    public void start() {

        new LoginEventGroup(LoginEventHandler.class, 2);
        EventDispatch.getInstance().start();
        RedisManager.getInstance();
        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();

        new ReceiveFromGate().start();

        NettyServer nettyServer = new NettyServer(ServerConfig.HTTP_SERVER_IP,ServerConfig.HTTP_PORT);
        nettyServer.doInitNetty();
    }

}
