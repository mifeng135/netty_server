package com.game.gate;

import core.util.ConfigUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertiesConfig {
    public static String serverIp;
    public static int port;

    public static int connectCenterSocketIndex;
    public static String connectCenterServerIp;
    public static int connectCenterServerPort;

    public static int connectLogicSocketIndex;
    public static String connectLogicServerIp;
    public static int connectLogicServerPort;

    public PropertiesConfig(String fileName) {
        ConfigUtil.loadFile(fileName);
        initData();
    }

    private void initData() {
        serverIp = ConfigUtil.getString("gate_server_ip");
        port = ConfigUtil.getInt("gate_server_port");

//        connectCenterServerIp = ConfigUtil.getString("connect_gate_center_server_ip");
//        connectCenterServerPort = ConfigUtil.getInt("connect_gate_center_server_port");
//        connectCenterSocketIndex = ConfigUtil.getInt("gate_center_socket_index");

        connectLogicServerIp = ConfigUtil.getString("logic_server_ip");
        connectLogicServerPort = ConfigUtil.getInt("logic_server_port");
        connectLogicSocketIndex = ConfigUtil.getInt("gate_logic_socket_index");

    }
}
