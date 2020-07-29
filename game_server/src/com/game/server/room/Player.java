package com.game.server.room;

/**
 * Created by Administrator on 2020/7/3.
 */
public class Player {

    private String name;
    private int roomId;
    private int id;
    private int position;
    private int ready;
    private int x;
    private int y;
    private byte serverKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

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

    public byte getServerKey() {
        return serverKey;
    }

    public void setServerKey(byte serverKey) {
        this.serverKey = serverKey;
    }
}
