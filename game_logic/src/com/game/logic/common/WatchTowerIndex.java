package com.game.logic.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchTowerIndex {
    private int x;
    private int y;

    public WatchTowerIndex(int xIndex, int yIndex) {
        x = xIndex;
        y = yIndex;
    }
}
