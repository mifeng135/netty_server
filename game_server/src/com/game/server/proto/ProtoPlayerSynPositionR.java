package com.game.server.proto;

/**
 * Created by Administrator on 2020/7/9.
 */
public class ProtoPlayerSynPositionR {

    private int x;
    private int y;
    private int direction;


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
