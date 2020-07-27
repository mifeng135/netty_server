package com.game.server.room;


import com.game.server.constant.GameConstant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.coreUtil.TimerHolder;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.proto.ProtoAirPlanePropS;
import com.game.server.proto.ProtoGameOverS;
import com.game.server.proto.ProtoTilePositionSynR;
import com.game.server.socket.SendToGate;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/7/3.
 */
public class Room {


    private int gameState = GameConstant.GAME_STATE_WAIT;
    private List<Player> roomPlayer = new CopyOnWriteArrayList<>();
    private List<ProtoTilePositionSynR.TileVec> tileList = new ArrayList<>();
    private int roomId;
    private int maxPlayer = 2;
    private int winId = -1;
    private String mapRes;
    private int mapWidth;
    private int mapHeight;
    private Timeout airPlaneTimeOut;

    public List<Player> getRoomPlayer() {
        return roomPlayer;
    }

    public void setRoomPlayer(List<Player> roomPlayer) {
        this.roomPlayer = roomPlayer;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public void addPlayer(Player player) {
        roomPlayer.add(player);
    }

    public void removePlyaer(int playerId) {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getId() == playerId) {
                roomPlayer.remove(i);
                break;
            }
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean getFull() {
        return roomPlayer.size() >= maxPlayer;
    }

    public void updatePlayerReadyState(int id, int state) {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getId() == id) {
                roomPlayer.get(i).setReady(state);
                break;
            }
        }
    }

    public boolean getPlayerAllReady() {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getReady() != 1) {
                return false;
            }
        }
        return true;
    }

    public void dismiss() {
        roomPlayer.clear();
    }

    public int getWinId() {
        return winId;
    }

    public void setWinId(int winId) {
        this.winId = winId;
    }

    public String getMapRes() {
        return mapRes;
    }

    public void setMapRes(String mapRes) {
        this.mapRes = mapRes;
    }

    public List<ProtoTilePositionSynR.TileVec> getTileList() {
        return tileList;
    }

    public void setTileList(List<ProtoTilePositionSynR.TileVec> tileList) {
        this.tileList = tileList;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void sendAirPlane() {
        airPlaneTimeOut = TimerHolder.getTimer().newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                sendAirPlaneMsg();
                airPlaneTimeOut = null;
            }
        }, 15, TimeUnit.SECONDS);
    }

    private void sendAirPlaneMsg() {
        Random random = new Random();
        int propCount = random.nextInt(2) + 1;
        List<ProtoAirPlanePropS.Vec> tempList = new ArrayList<>();

        List<Integer> randomList = new ArrayList<>();
        while (randomList.size() < propCount) {
            int value = random.nextInt(tileList.size());
            if (!randomList.contains(value)) {
                randomList.add(value);
            }
        }

        for (int i = 0; i < randomList.size(); i++) {
            ProtoTilePositionSynR.TileVec tileVec = tileList.get(randomList.get(i));
            ProtoAirPlanePropS.Vec vec = new ProtoAirPlanePropS.Vec();
            vec.setX(tileVec.getX());
            vec.setY(tileVec.getY());
            int type = random.nextInt(3);
            vec.setType(type);
            tempList.add(vec);
        }

        ProtoAirPlanePropS protoAirPlanePropS = new ProtoAirPlanePropS();
        protoAirPlanePropS.setPropList(tempList);

        for (int i = 0; i < roomPlayer.size(); i++) {
            Player player = roomPlayer.get(i);
            MsgBean msgBean = new MsgBean();
            msgBean.setId(player.getId());
            msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_AIRPLANE_PROP_S);
            msgBean.setData(ProtoUtil.serialize(protoAirPlanePropS));
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
        sendAirPlane();
    }

    public void clearTimeOut() {
        if (airPlaneTimeOut != null) {
            airPlaneTimeOut.cancel();
        }
    }

    public void gameOver() {
        TimerHolder.getTimer().newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                sendGameOverMsg();
            }
        }, 3, TimeUnit.SECONDS);
    }


    private void sendGameOverMsg() {
        ProtoGameOverS protoGameOverS = new ProtoGameOverS();
        protoGameOverS.setWinId(getWinId());
        for (int i = 0; i < roomPlayer.size(); i++) {
            Player player = roomPlayer.get(i);
            MsgBean msgBean = new MsgBean();
            msgBean.setId(player.getId());
            msgBean.setCmd(MsgCmdConstant.MSG_CMD_GAME_OVER_S);
            msgBean.setData(ProtoUtil.serialize(protoGameOverS));
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
        clear();
    }
    public void clear() {
        RoomManager.getInstance().removeRoom(getRoomId());
        for (int i = 0; i < roomPlayer.size(); i++) {
            PlayerManager.getInstance().removePlayer(roomPlayer.get(i).getId());
        }
        roomPlayer.clear();
    }
}
