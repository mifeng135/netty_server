package com.game.server;


import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpServer;
import core.util.ConfigUtil;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        ConfigUtil.loadFile("logic-config.properties");
        initAsyncHttp();
        initTcpServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LogicApplication.class.getName());
    }

    private static void initAsyncHttp() {
        AsyncHttp.getInstance().initBaseUrl(ConfigUtil.getString("db_http_url"));
        CtrlAnnotation.getInstance();
    }

    private static void initTcpServer() {
        String tcpIp = ConfigUtil.getString("logic_tcp_ip");
        int port = ConfigUtil.getInt("logic_tcp_port");
        new TcpServer(tcpIp, port, LOCAL).startServer();
    }
}
