package com.game.logic.model;

import com.game.logic.common.Position;
import com.game.logic.common.Rect;
import com.game.logic.manager.SceneManager;
import com.game.logic.manager.WatchTower;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Player {
    private int playerIndex;
    private String name;
    private String level;
    private int exp;
    private int sceneId;
    private Position position;
    private WatchTower watchTower;

    public Player(int playerIndex, Position position) {
        this.playerIndex = playerIndex;
        this.position = position;
        updateWatchTower();
    }

    public void updatePlayerPosition(Position position) {
        this.position = position;
        updateWatchTower();
    }

    public void updatePlayerPosition(int sceneId, Position position) {
        this.sceneId = sceneId;
        this.position = position;
        updateWatchTower();
    }

    public void updateWatchTower() {
        WatchTower currentWatchTower = SceneManager.getWatchTower(this);
        if (watchTower == null) {
            currentWatchTower.addPlayer(this);
            watchTower = currentWatchTower;
        } else {
            if (!currentWatchTower.equals(this.watchTower)) {
                watchTower.removePlayer(playerIndex);
                currentWatchTower.addPlayer(this);
                watchTower = currentWatchTower;
            }
        }

    }
}
