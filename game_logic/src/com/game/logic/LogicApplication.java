package com.game.logic;


import com.game.logic.manager.SceneManager;
import core.annotation.ctrl.CtrlA;
import core.annotation.table.TableA;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpServer;
import core.util.FileUtil;
import core.util.Util;
import org.apache.log4j.PropertyConfigurator;


import static core.Constants.LOCAL;

public class LogicApplication {

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileUtil.getFilePath("log4j.properties"));
        TableA.getInstance().init(Util.getPackageName(LogicApplication.class));
        new LoadConfig().load();
        new PropertiesConfig("config.properties");
        CtrlA.getInstance().init(Util.getPackageName(LogicApplication.class), new LogicExceptionHandler());
        new EventThreadGroup(Util.getRunProcessor() * 2, LogicEventHandler.class, LogicApplication.class.getName());
        new TcpServer(PropertiesConfig.serverIp, PropertiesConfig.serverPort, LOCAL).startServer();
        SceneManager.initSceneMap();
        AsyncHttp.getInstance().initBaseUrl("http://" + PropertiesConfig.dbServerIp + ":" + PropertiesConfig.dbServerPort);
    }
}
