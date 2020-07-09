package com.game.server.proto;

/**
 * Created by Administrator on 2020/7/9.
 */
public class ProtoPlayerPositionS {

    private int direction;
    private int position;
    private int id;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
