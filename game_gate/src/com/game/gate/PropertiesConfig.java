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

    public static int connectLogicSokcetIndex;
    public static String connectLogicServerIp;
    public static int connectLogicServerPort;

    public PropertiesConfig() {
        ConfigUtil.loadFile("gate-config.properties");
        initData();
    }

    private void initData() {
        serverIp = ConfigUtil.getString("gate_server_ip");
        port = ConfigUtil.getInt("gate_server_port");

        connectCenterSocketIndex = ConfigUtil.getInt("gate_center_socket_index");
        connectCenterServerIp = ConfigUtil.getString("connect_center_server_ip");
        connectCenterServerPort = ConfigUtil.getInt("connect_center_server_port");

        connectLogicSokcetIndex = ConfigUtil.getInt("gate_logic_socket_index");
        connectLogicServerIp = ConfigUtil.getString("connect_logic_server_ip");
        connectLogicServerPort = ConfigUtil.getInt("connect_logic_server_port");
    }
}
