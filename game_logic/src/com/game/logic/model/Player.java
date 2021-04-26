package com.game.logic.model;

import com.game.logic.common.Position;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Player {
    private int playerIndex;
    private String name;
    private String level;
    private int exp;
    private int mapId;
    private Position position;

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
