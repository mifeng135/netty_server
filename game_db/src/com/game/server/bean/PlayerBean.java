package com.game.server.bean;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/6/5.
 */

@Getter
@Setter
public class PlayerBean implements Serializable {
    private int id;
    private String name;
    private String account;
    private String password;
    private int registerTime;
    private String loginIp;
    private int lastLoginTime;
    private String header;
}
