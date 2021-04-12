package com.game.server.bean;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ServerListBean implements Serializable {
    private int id;
    private String serverName;
    private int serverId;
    private int state;
    private int openTime;
    private String serverIp;
}
