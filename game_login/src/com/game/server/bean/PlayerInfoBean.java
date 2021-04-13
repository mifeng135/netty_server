package com.game.server.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PlayerInfoBean implements Serializable {
    private int playerIndex;
    private String name;
}
