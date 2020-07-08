package com.game.server.proto;

/**
 * Created by Administrator on 2020/7/8.
 */
public class ProtoPlayer {

    private int id;
    private String name;
    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
