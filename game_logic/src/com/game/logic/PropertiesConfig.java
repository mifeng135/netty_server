package com.game.logic;

import core.util.ConfigUtil;

public class PropertiesConfig {


    public static String serverIp;
    public static int serverPort;

    public static int gateLogicSocketIndex;


    public static String dbServerIp;
    public static int dbServerPort;
    public static int logicDBSocketIndex;


    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    public void initData() {
        serverIp = ConfigUtil.getString("logic_server_ip");
        serverPort = ConfigUtil.getInt("logic_server_port");
        gateLogicSocketIndex = ConfigUtil.getInt("gate_logic_socket_index");

        dbServerIp = ConfigUtil.getString("db_server_ip");
        dbServerPort = ConfigUtil.getInt("db_server_port");
        logicDBSocketIndex = ConfigUtil.getInt("logic_db_socket_index");

    }
}
