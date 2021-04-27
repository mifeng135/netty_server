package com.game.logic;

import core.util.ConfigUtil;

public class PropertiesConfig {

    public static String httpDBUrl;

    public static String serverIp;
    public static int serverPort;

    public static int gateLogicSocketIndex;


    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    public void initData() {
        httpDBUrl = ConfigUtil.getString("db_http_url");

        serverIp = ConfigUtil.getString("logic_server_ip");
        serverPort = ConfigUtil.getInt("logic_server_port");

        gateLogicSocketIndex = ConfigUtil.getInt("gate_logic_socket_index");
    }
}
