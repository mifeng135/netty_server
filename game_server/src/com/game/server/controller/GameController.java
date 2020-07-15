package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.coreUtil.TimerHolder;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.map.MapUtil;
import com.game.server.map.MapVec;
import com.game.server.proto.*;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.socket.SendToGate;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2020/7/3.
 */

@Ctrl
public class GameController {

    /**
     * 玩家放置炸弹
     *
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_BOMB_PLACE_R)
    public void playerBombPlace(int id, byte[] data) {

        ProtoBombPlaceR protoBombPlaceR = ProtoUtil.deserializer(data, ProtoBombPlaceR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(id);
        List<Player> playerList = room.getRoomPlayer();

        ProtoBombPlaceS protoBombPlaceS = new ProtoBombPlaceS();
        protoBombPlaceS.setX(protoBombPlaceR.getX());
        protoBombPlaceS.setY(protoBombPlaceR.getY());

        for (int i = 0; i < playerList.size(); i++) {
            Player pl = playerList.get(i);
            MsgBean bean = new MsgBean();
            bean.setId(pl.getId());
            bean.setCmd(MsgCmdConstant.MSG_CMD_PLAYER_BOMB_PLACE_S);
            bean.setData(ProtoUtil.serialize(protoBombPlaceS));
            SendToGate.getInstance().pushSendMsg(bean);
        }
    }

    /**
     * 玩家同步位置
     *
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_SYN_POSITION_R)
    public void playerSynPosition(int id, byte[] data) {

        ProtoPlayerSynPositionR protoPlayerPositionR = ProtoUtil.deserializer(data, ProtoPlayerSynPositionR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(id);


        if (room == null) {
            return;
        }
        List<Player> playerList = room.getRoomPlayer();

        Player player = PlayerManager.getInstance().getPlayer(id);
        player.setX(protoPlayerPositionR.getX());
        player.setY(protoPlayerPositionR.getY());

        ProtoPlayerSynPositionS protoPlayerSynPositionS = new ProtoPlayerSynPositionS();
        protoPlayerSynPositionS.setId(id);
        protoPlayerSynPositionS.setX(protoPlayerPositionR.getX());
        protoPlayerSynPositionS.setY(protoPlayerPositionR.getY());
        protoPlayerSynPositionS.setDirection(protoPlayerPositionR.getDirection());

        for (int i = 0; i < playerList.size(); i++) {
            Player pl = playerList.get(i);
            if (pl.getId() == id) {
                continue;
            }
            MsgBean bean = new MsgBean();
            bean.setId(pl.getId());
            bean.setCmd(MsgCmdConstant.MSG_CMD_PLAYER_SYN_POSITION_S);
            bean.setData(ProtoUtil.serialize(protoPlayerSynPositionS));
            SendToGate.getInstance().pushSendMsg(bean);
        }
    }

    /**
     * 炸弹爆炸
     *
     * @param id
     * @param data
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_BOMB_EXPLODE_R)
    public void bombExplode(int id, byte[] data) {
        ProtoBombExplodeR protoBombExplodeR = ProtoUtil.deserializer(data, ProtoBombExplodeR.class);
        List<ProtoBombExplodeR.Vec> explodePath = protoBombExplodeR.getExplodePath();
        Room room = RoomManager.getInstance().getRoomByPlayerId(id);
        List<Player> playerList = room.getRoomPlayer();

        List<Integer> deadList = new ArrayList<>();

        for (int i = 0; i < explodePath.size(); i++) {
            ProtoBombExplodeR.Vec vec = explodePath.get(i);
            for (int index = 0; index < playerList.size(); index++) {
                Player player = playerList.get(index);
                int x = player.getX();
                int y = player.getY();
                MapVec mapVec = MapUtil.getTilePosition(x, y);
                if (mapVec.getX() == vec.getX() && mapVec.getY() == vec.getY()) {
                    deadList.add(player.getId());
                }
            }
        }

        if (deadList.size() > 0) {
            ProtoBombExplodeS protoBombExplodeS = new ProtoBombExplodeS();
            protoBombExplodeS.setDeadList(deadList);


            int winId = -1;

            for (int i = 0; i < playerList.size(); i++) {

                Player player = playerList.get(i);
                if (!deadList.contains(player.getId())) {
                    winId = player.getId();
                }
                MsgBean msgBean = new MsgBean();
                msgBean.setId(player.getId());
                msgBean.setCmd(MsgCmdConstant.MSG_CMD_BOMB_EXPLODE_S);
                msgBean.setData(ProtoUtil.serialize(protoBombExplodeS));
                SendToGate.getInstance().pushSendMsg(msgBean);
            }
            room.setWinId(winId);
            room.gameOver();
        }
    }
}
