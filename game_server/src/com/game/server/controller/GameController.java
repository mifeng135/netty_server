package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.map.MapUtil;
import com.game.server.map.MapVec;
import com.game.server.proto.*;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.util.SocketUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2020/7/3.
 */

@Ctrl
public class GameController {

    /**
     * 玩家放置炸弹
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_BOMB_PLACE_R)
    public void playerBombPlace(MsgBean msgBean) {

        ProtoBombPlaceR protoBombPlaceR = ProtoUtil.deserializer(msgBean.getData(), ProtoBombPlaceR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());

        ProtoBombPlaceS protoBombPlaceS = new ProtoBombPlaceS();
        protoBombPlaceS.setX(protoBombPlaceR.getX());
        protoBombPlaceS.setY(protoBombPlaceR.getY());
        protoBombPlaceS.setPower(protoBombPlaceR.getPower());
        protoBombPlaceS.setPlayerId(protoBombPlaceR.getPlayerId());

        SocketUtil.sendToRoom(MsgCmdConstant.MSG_CMD_PLAYER_BOMB_PLACE_S,protoBombPlaceS,room);
    }

    /**
     * 玩家同步位置
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_PLAYER_SYN_POSITION_R)
    public void playerSynPosition(MsgBean msgBean) {

        ProtoPlayerSynPositionR protoPlayerPositionR = ProtoUtil.deserializer(msgBean.getData(), ProtoPlayerSynPositionR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());
        if (room == null) {
            return;
        }
        Player player = PlayerManager.getInstance().getPlayer(msgBean.getId());
        player.setX(protoPlayerPositionR.getX());
        player.setY(protoPlayerPositionR.getY());

        ProtoPlayerSynPositionS protoPlayerSynPositionS = new ProtoPlayerSynPositionS();
        protoPlayerSynPositionS.setId(msgBean.getId());
        protoPlayerSynPositionS.setX(protoPlayerPositionR.getX());
        protoPlayerSynPositionS.setY(protoPlayerPositionR.getY());
        protoPlayerSynPositionS.setDirection(protoPlayerPositionR.getDirection());

        SocketUtil.sendToRoomExcept(MsgCmdConstant.MSG_CMD_PLAYER_SYN_POSITION_S,protoPlayerSynPositionS,room,msgBean.getId());
    }


    /**
     * 炸弹爆炸的时候产生道具
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_CREATE_PROP_R)
    public void createProp(MsgBean msgBean) {
        ProtoPropCreatorR protoPropCreatorR = ProtoUtil.deserializer(msgBean.getData(), ProtoPropCreatorR.class);

        Random random = new Random();
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());

        int value = random.nextInt(5);
        if (value > 3) {
            List<ProtoPropCreatorR.Vec> removePath = protoPropCreatorR.getRemovePath();
            int propPosition = random.nextInt(removePath.size());

            ProtoPropCreatorR.Vec vec = removePath.get(propPosition);
            List<ProtoPropCreatorS.PropVec> tempList = new ArrayList<>();

            ProtoPropCreatorS.PropVec propVec = new ProtoPropCreatorS.PropVec();
            propVec.setX(vec.getX());
            propVec.setY(vec.getY());
            int type = random.nextInt(3);
            propVec.setType(type);
            tempList.add(propVec);

            ProtoPropCreatorS protoPropCreatorS = new ProtoPropCreatorS();
            protoPropCreatorS.setPropList(tempList);
            SocketUtil.sendToRoom(MsgCmdConstant.MSG_CMD_GAME_CREATE_PROP_S,protoPropCreatorS,room);
        }
    }

    /**
     * 同步地图信息
     * @param msgBean
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_TILE_POSITION_SYN_R)
    public void tileSyn(MsgBean msgBean) {
        ProtoTilePositionSynR protoTilePositionSynR = ProtoUtil.deserializer(msgBean.getData(), ProtoTilePositionSynR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());
        room.setMapHeight(protoTilePositionSynR.getMapHeight());
        room.setMapWidth(protoTilePositionSynR.getMapWidth());
        room.setTileList(protoTilePositionSynR.getTileList());
    }

    /**
     * 玩家吃道具
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_TRIGGER_PROP_R)
    public void triggerProp(MsgBean msgBean) {
        ProtoTriggerPropR protoTriggerPropR = ProtoUtil.deserializer(msgBean.getData(), ProtoTriggerPropR.class);
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());
        ProtoTriggerPropS protoTriggerPropS = new ProtoTriggerPropS();
        protoTriggerPropS.setX(protoTriggerPropR.getX());
        protoTriggerPropS.setY(protoTriggerPropR.getY());
        SocketUtil.sendToRoomExcept(MsgCmdConstant.MSG_CMD_GAME_TRIGGER_PROP_S,protoTriggerPropS,room,msgBean.getId());
    }

    /**
     * 炸弹爆炸
     */
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_BOMB_EXPLODE_R)
    public void bombExplode(MsgBean msgBean) {
        ProtoBombExplodeR protoBombExplodeR = ProtoUtil.deserializer(msgBean.getData(), ProtoBombExplodeR.class);
        List<ProtoBombExplodeR.Vec> explodePath = protoBombExplodeR.getExplodePath();
        Room room = RoomManager.getInstance().getRoomByPlayerId(msgBean.getId());
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
            }

            SocketUtil.sendToRoom(MsgCmdConstant.MSG_CMD_BOMB_EXPLODE_S,protoBombExplodeS,room);
            room.setWinId(winId);
            room.clearTimeOut();
            room.gameOver();
        }
    }
}
