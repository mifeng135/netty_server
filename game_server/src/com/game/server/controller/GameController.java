package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.proto.*;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.socket.SendToGate;

import java.util.List;


/**
 * Created by Administrator on 2020/7/3.
 */

@Ctrl
public class GameController {

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

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_POSITION_R)
    public void playerPosition(int id, byte[] data) {
        Player player = PlayerManager.getInstance().getPlayer(id);

        ProtoPlayerPositionR protoPlayerPositionR = ProtoUtil.deserializer(data, ProtoPlayerPositionR.class);

        Room room = RoomManager.getInstance().getRoomByPlayerId(id);
        List<Player> playerList = room.getRoomPlayer();

        ProtoPlayerPositionS protoPlayerPositionS = new ProtoPlayerPositionS();
        protoPlayerPositionS.setDirection(protoPlayerPositionR.getDirection());
        protoPlayerPositionS.setPosition(player.getPosition());
        protoPlayerPositionS.setId(player.getId());

        for (int i = 0; i < playerList.size(); i++) {
            Player pl = playerList.get(i);
            MsgBean bean = new MsgBean();
            bean.setId(pl.getId());
            bean.setCmd(MsgCmdConstant.MSG_CMD_PLAYER_POSITION_S);
            bean.setData(ProtoUtil.serialize(protoPlayerPositionS));
            SendToGate.getInstance().pushSendMsg(bean);
        }
    }
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_SYN_POSITION_R)
    public void playerSynPosition(int id, byte[] data) {

        ProtoPlayerSynPositionR protoPlayerPositionR = ProtoUtil.deserializer(data, ProtoPlayerSynPositionR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(id);
        List<Player> playerList = room.getRoomPlayer();

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
}
