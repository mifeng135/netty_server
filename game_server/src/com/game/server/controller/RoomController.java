package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.Constant;
import com.game.server.constant.GameConstant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.redis.RedisManager;
import com.game.server.map.MapConfig;
import com.game.server.proto.*;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.socket.SendToGate;
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
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R)
    public void createRoom(int id, byte[] data) {
        ProtoCreateRoomS protoCreateRoomS = new ProtoCreateRoomS();
        if (PlayerManager.getInstance().getPlayer(id) != null) {
            protoCreateRoomS.setRet(-1);
            MsgBean msgBean = new MsgBean();
            msgBean.setId(id);
            msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_S);
            msgBean.setData(ProtoUtil.serialize(protoCreateRoomS));
            SendToGate.getInstance().pushSendMsg(msgBean);
            return;
        }

        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(id);

        int roomId = generatorRoomId();
        Player player = new Player();
        player.setId(id);
        player.setRoomId(roomId);
        player.setName(bean.getName());
        player.setPosition(0);
        player.setX(MapConfig.PLAYER1_X);
        player.setY(MapConfig.PLAYER1_Y);

        Room room = new Room();
        room.setRoomId(roomId);
        room.addPlayer(player);
        RoomManager.getInstance().putRoom(roomId, room);

        PlayerManager.getInstance().putPlayer(player);
        protoCreateRoomS.setRet(0);


        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_S);
        msgBean.setData(ProtoUtil.serialize(protoCreateRoomS));
        SendToGate.getInstance().pushSendMsg(msgBean);
    }

    /**
     * 加入房间
     *
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R)
    public void joinRoom(int id, byte[] data) {
        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(id);
        Player player = new Player();
        player.setId(id);
        player.setName(bean.getName());
        player.setPosition(1);
        player.setX(MapConfig.PLAYER2_X);
        player.setY(MapConfig.PLAYER2_Y);


        Room room = this.findWaitJoinRoom();
        ProtoJoinRoomS joinRoomS = new ProtoJoinRoomS();
        if (room != null) {
            PlayerManager.getInstance().putPlayer(player);
            player.setRoomId(room.getRoomId());
            joinRoomS.setRet(0);
            room.addPlayer(player);
        } else {
            joinRoomS.setRet(-1);
        }
        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_S);
        msgBean.setData(ProtoUtil.serialize(joinRoomS));
        SendToGate.getInstance().pushSendMsg(msgBean);
    }

    /**
     * 玩家掉线
     *
     * @param id   玩家id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R)
    public void linkBroke(int id, byte[] data) {
        Player removePlayer = PlayerManager.getInstance().removePlayer(id);
        int roomId = removePlayer.getRoomId();
        Room room = RoomManager.getInstance().removeRoom(roomId);

        if (room == null) {
            return;
        }
        List<Player> players = room.getRoomPlayer();

        ProtoPlayerLeftS playerLeftS = new ProtoPlayerLeftS();
        playerLeftS.setLeftId(id);
        MsgBean msgBean = new MsgBean();

        for (int i = 0; i < players.size(); i++) {
            Player pl = players.get(i);
            msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_PLAYER_LEFT_ROOM_S);
            msgBean.setId(pl.getId());
            msgBean.setData(ProtoUtil.serialize(playerLeftS));
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
        room.dismiss();
    }

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_READY_R)
    public void ready(int id, byte[] data) {
        Player player = PlayerManager.getInstance().getPlayer(id);
        int roomId = player.getRoomId();
        Room room = RoomManager.getInstance().getRoom(roomId);
        room.updatePlayerReadyState(id, 1);

        if (room.getPlayerAllReady() && room.getFull()) {
            room.setGameState(GameConstant.GAME_STATE_PLAYING);
            sendGameStart(room);
        }
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

        for (int i = 0; i < roomPlayer.size(); i++) {
            MsgBean msgBean = new MsgBean();
            Player pl = roomPlayer.get(i);
            msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_START_S);
            msgBean.setId(pl.getId());
            msgBean.setData(ProtoUtil.serialize(gameStartS));
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
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
}
