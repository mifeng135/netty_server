package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.Constant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.redis.RedisManager;
import com.game.server.proto.ProtoCreateRoomS;
import com.game.server.proto.ProtoJoinRoomS;
import com.game.server.proto.ProtoPlayerLeftS;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.socket.SendToGate;
import org.redisson.api.RMap;

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
        Player player = new Player();
        player.setId(id);
        if (bean != null) {
            player.setName(bean.getName());
        }
        int roomId = generatorRoomId();
        Room room = new Room();
        room.setRoomId(roomId);

        RoomManager.getInstance().putRoom(roomId, room);
        room.addPlayer(player);

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
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R)
    public void joinRoom(int id, byte[] data) {
        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(id);
        Player player = new Player();
        player.setId(id);
        if (bean != null) {
            player.setName(bean.getName());
        }
        Room room = this.findWaitJoinRoom();

        ProtoJoinRoomS joinRoomS = new ProtoJoinRoomS();
        if (room != null) {
            joinRoomS.setRet(0);
            room.addPlayer(player);
        }else {
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
     * @param id 玩家id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R)
    public void linkBroke(int id, byte[] data) {
        Player removePlayer = PlayerManager.getInstance().removePlayer(id);
        int roomId = removePlayer.getRoomId();
        Room room = RoomManager.getInstance().removeRoom(roomId);
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
    }

    /**
     * 生成房间号
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

    /**
     * 查找是否有人等待的房间
     * @return
     */
    private Room findWaitJoinRoom() {
        Room room = null;
        ConcurrentHashMap<Integer, Room> gameRoom = RoomManager.getInstance().getGameRoom();
        for (ConcurrentHashMap.Entry<Integer, Room> entry : gameRoom.entrySet()) {
            room = entry.getValue();
            if (room.getGameState() == Constant.GAME_STATE_WAIT && !room.getFull()) {
                break;
            }
        }
        return room;
    }
}
