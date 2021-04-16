package com.game.gate;

import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;

import static core.Constants.REMOTE;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    public static void main(String[] arg) {
        CtrlAnnotation.getInstance().init(GateApplication.class.getPackage().getName());
        new PropertiesConfig();
        initGateServer();
        initCenterConnect();
        initLogicConnect();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), CustomEventHandler.class, GateApplication.class.getName());
    }

    private static void initGateServer() {
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.port, REMOTE).startServer();
    }

    private static void initCenterConnect() {
        new TcpConnection(PropertiesConfig.connectCenterSocketIndex).connect(PropertiesConfig.connectCenterServerIp, PropertiesConfig.connectCenterServerPort);
    }

    private static void initLogicConnect() {
        new TcpConnection(PropertiesConfig.connectLogicSokcetIndex).connect(PropertiesConfig.connectLogicServerIp, PropertiesConfig.connectLogicServerPort);
    }
}
