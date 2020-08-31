package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.Constant;
import com.game.server.constant.GameConstant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.redis.RedisManager;
import com.game.server.map.MapConfig;
import com.game.server.proto.*;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.util.SocketUtil;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create room is not sale thread function
 * must process at one thread
 * Created by Administrator on 2020/6/18.
 */

@Ctrl
public class RoomController {

    /**
     * 创建房间
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R)
    public void createRoom(MsgBean msgBean) {
        ProtoCreateRoomS protoCreateRoomS = new ProtoCreateRoomS();
        if (PlayerManager.getInstance().getPlayer(msgBean.getId()) != null) {
            SocketUtil.sendToPlayer(MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_S, protoCreateRoomS, msgBean.getId());
            return;
        }

        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(msgBean.getId());

        int roomId = generatorRoomId();
        Player player = new Player();
        player.setId(msgBean.getId());
        player.setRoomId(roomId);
        player.setName(bean.getName());
        player.setPosition(0);
        player.setServerKey(msgBean.getServerKey());

        Random rand = new Random();
        int randMap = rand.nextInt(126);
        String mapRes = MapConfig.MAP_RES.get(randMap);
        protoCreateRoomS.setMapRes(mapRes);
        protoCreateRoomS.setRet(0);


        Room room = new Room();
        room.setRoomId(roomId);
        room.addPlayer(player);
        room.setMapRes(mapRes);
        RoomManager.getInstance().putRoom(roomId, room);

        PlayerManager.getInstance().putPlayer(player);

        SocketUtil.sendToPlayer(MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_S, protoCreateRoomS, player.getId());

        ProtoRoomListS protoRoomListS = getRoomListMsg();
        SocketUtil.sendBroadCase(MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_S, protoRoomListS);
    }

    /**
     * 加入房间
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R)
    public void joinRoom(MsgBean msgBean) {
        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(msgBean.getId());

        Player player = new Player();
        player.setId(msgBean.getId());
        player.setName(bean.getName());
        player.setPosition(1);
        player.setServerKey(msgBean.getServerKey());

        Room room = this.findWaitJoinRoom();
        ProtoJoinRoomS joinRoomS = new ProtoJoinRoomS();
        if (room != null) {
            PlayerManager.getInstance().putPlayer(player);
            player.setRoomId(room.getRoomId());
            joinRoomS.setRet(0);
            joinRoomS.setMapRes(room.getMapRes());
            room.addPlayer(player);
        } else {
            joinRoomS.setRet(-1);
        }
        SocketUtil.sendToPlayer(MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_S, joinRoomS, msgBean.getId());
    }

    /**
     * 玩家掉线
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R)
    public void linkBroke(MsgBean msgBean) {
        Player removePlayer = PlayerManager.getInstance().getPlayer(msgBean.getId());
        if (removePlayer == null) {
            return;
        }
        int roomId = removePlayer.getRoomId();
        Room room = RoomManager.getInstance().getRoom(roomId);

        if (room == null) {
            return;
        }
        ProtoPlayerLeftS playerLeftS = new ProtoPlayerLeftS();
        playerLeftS.setLeftId(msgBean.getId());
        SocketUtil.sendToRoomExcept(MsgCmdConstant.MSG_CMD_GAME_PLAYER_LEFT_ROOM_S, playerLeftS, room, msgBean.getId());
        room.dismiss();
    }

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_READY_R)
    public void ready(MsgBean msgBean) {
        Player player = PlayerManager.getInstance().getPlayer(msgBean.getId());
        int roomId = player.getRoomId();
        Room room = RoomManager.getInstance().getRoom(roomId);
        if (room.getGameState() == GameConstant.GAME_STATE_PLAYING) {
            return;
        }
        room.updatePlayerReadyState(msgBean.getId(), 1);
        if (room.getPlayerAllReady() && room.getFull()) {
            room.setGameState(GameConstant.GAME_STATE_PLAYING);
            sendGameStart(room);
            room.sendAirPlane();
        }
    }


    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_EXIT_ROOM_R)
    public void exitRoom(MsgBean msgBean) {
        Player player = PlayerManager.getInstance().getPlayer(msgBean.getId());
        int roomId = player.getRoomId();
        Room room = RoomManager.getInstance().getRoom(roomId);
        ProtoExitRoomS protoExitRoomS = new ProtoExitRoomS();
        SocketUtil.sendToRoom(MsgCmdConstant.MSG_CMD_GAME_EXIT_ROOM_S, protoExitRoomS, room);
        room.clear();
        ProtoRoomListS protoRoomListS = getRoomListMsg();
        SocketUtil.sendBroadCase(MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_S, protoRoomListS);
    }

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_R)
    public void roomList(MsgBean msgBean) {
        ProtoRoomListS protoRoomListS = getRoomListMsg();
        SocketUtil.sendToPlayer(MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_S, protoRoomListS, msgBean.getId(), msgBean.getServerKey());
    }

    private void sendGameStart(Room room) {
        List<Player> roomPlayer = room.getRoomPlayer();
        ProtoGameStartS gameStartS = new ProtoGameStartS();
        List<ProtoPlayer> tempList = new ArrayList<>();
        for (int i = 0; i < roomPlayer.size(); i++) {
            Player pl = roomPlayer.get(i);
            ProtoPlayer protoPlayer = new ProtoPlayer();
            protoPlayer.setId(pl.getId());
            protoPlayer.setPosition(pl.getPosition());
            protoPlayer.setName(pl.getName());
            protoPlayer.setX(pl.getX());
            protoPlayer.setY(pl.getY());
            tempList.add(protoPlayer);
        }
        gameStartS.setPlayerList(tempList);
        SocketUtil.sendToRoom(MsgCmdConstant.MSG_CMD_GAME_START_S, gameStartS, room);
    }

    /**
     * 查找是否有人等待的房间
     *
     * @return
     */
    private Room findWaitJoinRoom() {
        Room room = null;
        ConcurrentHashMap<Integer, Room> gameRoom = RoomManager.getInstance().getGameRoom();
        for (ConcurrentHashMap.Entry<Integer, Room> entry : gameRoom.entrySet()) {
            Room value = entry.getValue();
            if (value.getGameState() == GameConstant.GAME_STATE_WAIT && !value.getFull()) {
                room = value;
                break;
            }
        }
        return room;
    }

    /**
     * 生成房间号
     *
     * @return
     */
    private int generatorRoomId() {
        int roomId;
        ConcurrentHashMap<Integer, Room> gameRoom = RoomManager.getInstance().getGameRoom();
        while (true) {
            roomId = new Random().nextInt(999999);
            if (roomId < 100000) {
                roomId += 100000;
            }
            if (gameRoom.get(roomId) == null) {
                break;
            }
        }
        return roomId;
    }

    private ProtoRoomListS getRoomListMsg() {
        List<ProtoRoomListS.RoomInfo> roomList = new ArrayList<>();
        ConcurrentHashMap<Integer, Room> gameRoom = RoomManager.getInstance().getGameRoom();
        for (ConcurrentHashMap.Entry<Integer, Room> entry : gameRoom.entrySet()) {
            Room value = entry.getValue();
            if (value.getGameState() == GameConstant.GAME_STATE_WAIT) {
                ProtoRoomListS.RoomInfo roomInfo = new ProtoRoomListS.RoomInfo();
                roomInfo.setRoomId(value.getRoomId());
                roomInfo.setRoomState(value.getGameState());
                roomList.add(roomInfo);
            }
        }
        ProtoRoomListS protoRoomListS = new ProtoRoomListS();
        protoRoomListS.setRoomList(roomList);
        return protoRoomListS;
    }
}
