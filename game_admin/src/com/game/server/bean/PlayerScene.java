package com.game.server.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerScene {
    private int id;
    private int playerIndex;
    private float playerPositionX;
    private float playerPositionY;
    private int sceneId;
}
