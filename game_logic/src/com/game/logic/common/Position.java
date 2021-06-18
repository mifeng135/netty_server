package com.game.logic.common;


import lombok.Getter;
import lombok.Setter;

import static com.game.logic.Constants.GRID_HEIGHT;
import static com.game.logic.Constants.GRID_WIDTH;

@Getter
@Setter
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 转化为格子id
     *
     * @param cnx 行数
     * @return
     */
    public int getGridId(int cnx) {
        int column = (int) Math.floor(x / GRID_WIDTH);
        int row = (int) Math.floor(y / GRID_HEIGHT);
        return row * cnx + column;
    }
}
