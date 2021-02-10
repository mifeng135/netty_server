package com.game.server.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerItem {
    private int id;
    private int playerIndex;
    private int itemId;
    private int itemCount;
}
