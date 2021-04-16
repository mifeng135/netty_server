package com.game.center;


import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static config.Config.*;
import static core.Constants.LOCAL;

public class CenterApplication {
    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);

    public static void main(String[] args) {
        CtrlAnnotation.getInstance().init(CenterApplication.class.getPackage().getName());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), CenterApplication.class.getName());
        new TcpServer(CENTER_SERVER_IP, CENTER_SERVER_PORT, LOCAL).startServer();
    }
}
