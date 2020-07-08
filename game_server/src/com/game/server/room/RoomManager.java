package com.game.server.room;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/7/3.
 */
public class RoomManager {

    private static class DefaultInstance {
        static final RoomManager INSTANCE = new RoomManager();
    }

    public static RoomManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private ConcurrentHashMap<Integer, Room> gameRoom = new ConcurrentHashMap<>();

    public void putRoom(int roomID, Room room) {
        gameRoom.putIfAbsent(roomID, room);
    }

    public Room removeRoom(int roomId) {
        return gameRoom.remove(roomId);
    }


    public Room getRoom(int roomId) {
        return gameRoom.get(roomId);
    }
    public ConcurrentHashMap getGameRoom() {
        return gameRoom;
    }

}
