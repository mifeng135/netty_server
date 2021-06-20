package com.game.gate;

import core.annotation.ctrl.CtrlA;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static core.Constants.REMOTE;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    public static void main(String[] arg) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        CtrlA.getInstance().init(GateApplication.class.getPackage().getName(), new GateExceptionHandler());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), GateEventHandler.class, GateApplication.class.getName());

        new PropertiesConfig("config.properties");
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.port, REMOTE).startServer();
        new TcpConnection(PropertiesConfig.connectLogicSocketIndex).connect(PropertiesConfig.connectLogicServerIp, PropertiesConfig.connectLogicServerPort);
    }
}
