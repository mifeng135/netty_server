package com.game.logic;


import com.game.logic.manager.SceneManager;
import core.annotation.CA;
import core.annotation.TA;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        TA.getInstance().init(LogicApplication.class.getPackage().getName());
        new LoadConfig().load();
        new PropertiesConfig("config.properties");
        CA.getInstance().init(LogicApplication.class.getPackage().getName(), new LogicExceptionHandler());
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
        new TcpConnection(PropertiesConfig.logicDBSocketIndex).connect(PropertiesConfig.dbServerIp, PropertiesConfig.dbServerPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, LogicApplication.class.getName());
        SceneManager.initSceneMap();
    }
}
