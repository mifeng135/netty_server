package com.game.logic;


import com.game.logic.manager.SceneManager;
import core.annotation.ctrl.CtrlA;
import core.annotation.table.TableA;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;
import core.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        TableA.getInstance().init(LogicApplication.class.getPackage().getName());
        new LoadConfig().load();
        new PropertiesConfig("config.properties");
        CtrlA.getInstance().init(LogicApplication.class.getPackage().getName(), new LogicExceptionHandler());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors() * 2, LogicApplication.class.getName());
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
        SceneManager.initSceneMap();

        AsyncHttp.getInstance().initBaseUrl(PropertiesConfig.dbServerIp + ":" + PropertiesConfig.dbServerPort);
    }
}
