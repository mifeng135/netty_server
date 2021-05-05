package com.game.logic;


import com.game.logic.manager.SceneManager;
import core.annotation.CtrlAnnotation;
import core.annotation.TableAnnotation;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;
import core.util.ConfigUtil;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        TableAnnotation.getInstance().init(LogicApplication.class.getPackage().getName());
        new LoadConfig().load();
        new PropertiesConfig("config.properties");
        CtrlAnnotation.getInstance().init(LogicApplication.class.getPackage().getName(), new LogicExceptionHandler());
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
        new TcpConnection(PropertiesConfig.logicDBSocketIndex).connect(PropertiesConfig.dbServerIp, PropertiesConfig.dbServerPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, LogicApplication.class.getName());
        SceneManager.initSceneMap();
    }
}
