package com.game.logic;


import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpServer;
import core.util.ConfigUtil;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        new PropertiesConfig("logic-config.properties");
        CtrlAnnotation.getInstance().init(LogicApplication.class.getPackage().getName(), new LogicExceptionHandler());
        AsyncHttp.getInstance().initBaseUrl(PropertiesConfig.httpDBUrl);
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, LogicApplication.class.getName());
    }
}
