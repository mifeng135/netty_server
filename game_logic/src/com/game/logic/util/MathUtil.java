package com.game.logic.util;

import protocal.remote.common.Position;

import static com.game.logic.Constants.GRID_HEIGHT;
import static com.game.logic.Constants.GRID_WIDTH;
import static core.Constants.CLIENT_FRAME_COUNT;

public class MathUtil {


    private static final float speed = 1 / 60f;

    /**
     * 修正延迟带来的位置差异
     *
     * @param x
     * @param y
     * @param delayTime 延迟时间
     * @param moveAngle 移动角度
     */
    public static Position calcDelayTimePosition(int x, int y, float delayTime, float moveAngle) {
        float count = delayTime / CLIENT_FRAME_COUNT;
        int xSpeed = (int) (Math.cos(moveAngle) * speed * count);
        int ySpeed = (int) (Math.sin(moveAngle) * speed * count);
        return new Position(x + xSpeed, y + ySpeed);
    }

    /**
     * 获取所在的地图的格子id
     * @param x
     * @param y
     * @param cnx
     * @return
     */
    public static int getGridId(int x, int y, int cnx) {
        int column = (int) Math.floor(x / GRID_WIDTH);
        int row = (int) Math.floor(y / GRID_HEIGHT);
        return row * cnx + column;
    }
}
