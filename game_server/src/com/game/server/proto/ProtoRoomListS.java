package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/16.
 */
public class ProtoRoomListS {

    private List<RoomInfo> roomList;


    public List<RoomInfo> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomInfo> roomList) {
        this.roomList = roomList;
    }

    public static class RoomInfo {
        private int roomId;
        private int roomState;

        public int getRoomState() {
            return roomState;
        }

        public void setRoomState(int roomState) {
            this.roomState = roomState;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

    }
}
