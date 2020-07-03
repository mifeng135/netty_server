package com.game.game.room;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/7/3.
 */
public class RoomManger {

    private static class DefaultInstance {
        static final RoomManger INSTANCE = new RoomManger();
    }

    public static RoomManger getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private ConcurrentHashMap<Integer, Room> gameRoom = new ConcurrentHashMap<>();

    public void putRoom(int roomID, Room room) {
        gameRoom.putIfAbsent(roomID, room);
    }

    public Room removeRoom(int roomId) {
        return gameRoom.remove(roomId);
    }

}
