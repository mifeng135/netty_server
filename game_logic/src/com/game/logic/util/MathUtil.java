package com.game.logic.util;

import protocal.remote.common.Position;

import static core.Constants.CLIENT_FRAME_COUNT;

public class MathUtil {

    /**
     * 修正延迟带来的位置差异
     * @param position 客户端position
     * @param delayTime 延迟时间
     * @param moveAngle 移动角度
     * @param speed 移动速度
     */
    public static void calcDelayTimePosition(Position position, float delayTime, float moveAngle, float speed) {
        float count = delayTime / CLIENT_FRAME_COUNT;
        int xSpeed = (int) (Math.cos(moveAngle) * speed * count);
        int ySpeed = (int) (Math.sin(moveAngle) * speed * count);
        position.sub(xSpeed, ySpeed);
    }
}
