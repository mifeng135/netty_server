package com.game.logic;


import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpServer;
import core.util.ConfigUtil;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        new PropertiesConfig();
        CtrlAnnotation.getInstance().init(LogicApplication.class.getPackage().getName());
        initAsyncHttp();
        initTcpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LogicApplication.class.getName());
    }

    private static void initAsyncHttp() {
        AsyncHttp.getInstance().initBaseUrl(PropertiesConfig.httpDBUrl);
    }

    private static void initTcpServer() {
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
    }
}
