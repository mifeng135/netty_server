package com.game.center;


import core.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CenterApplication {
    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);

    public static void main(String[] args) {
//        CtrlAnnotation.getInstance().init(CenterApplication.class.getPackage().getName());
//        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), CenterApplication.class.getName());
//        new TcpServer(CENTER_SERVER_IP, CENTER_SERVER_PORT, LOCAL).startServer();
    }
}
