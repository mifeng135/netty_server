package com.game.server.util;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.manager.SendSocketManager;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.zero.Send;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/7/28.
 */
public class SocketUtil {


    public static void sendToRoom(int cmd, Object msg, Room room) {
        byte[] data = ProtoUtil.serialize(msg);
        List<Player> playerList = room.getRoomPlayer();

        List<Byte> serverKeyList = SendSocketManager.getInstance().getSendServerKeyList();
        List<Integer> sendList = new ArrayList<>();
        for (int index = 0; index < serverKeyList.size(); index++) {
            byte key = serverKeyList.get(index);
            sendList.clear();
            for (int i = 0; i < playerList.size(); i++) {
                Player player = playerList.get(i);
                if (player.getServerKey() == key) {
                    sendList.add(player.getId());
                }
            }
            if (sendList.size() > 0) {
                MsgBean msgBean = new MsgBean();
                msgBean.setCmd(cmd);
                msgBean.setSubCmd(MsgCmdConstant.MSG_BROAD_CASE_GROUP);
                msgBean.setArrayData(sendList);
                msgBean.setData(data);
                SendSocketManager.getInstance().getSocket(key).pushSendMsg(msgBean);
            }
        }
    }

    public static void sendToPlayer(int cmd, Object msg, int id) {
        Player player = PlayerManager.getInstance().getPlayer(id);
        if (player == null) {
            return;
        }
        byte[] data = ProtoUtil.serialize(msg);
        MsgBean msgBean = new MsgBean();
        msgBean.setId(player.getId());
        msgBean.setCmd(cmd);
        msgBean.setData(data);
        SendSocketManager.getInstance().getSocket(player.getServerKey()).pushSendMsg(msgBean);
    }

    public static void sendToPlayer(int cmd, Object msg, int id, byte serverKey) {
        byte[] data = ProtoUtil.serialize(msg);
        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setCmd(cmd);
        msgBean.setData(data);
        SendSocketManager.getInstance().getSocket(serverKey).pushSendMsg(msgBean);
    }

    public static void sendToRoomExcept(int cmd, Object msg, Room room, int id) {
        byte[] data = ProtoUtil.serialize(msg);
        List<Player> playerList = room.getRoomPlayer();

        List<Byte> serverKeyList = SendSocketManager.getInstance().getSendServerKeyList();
        List<Integer> sendList = new ArrayList<>();
        for (int index = 0; index < serverKeyList.size(); index++) {
            byte key = serverKeyList.get(index);
            sendList.clear();
            for (int i = 0; i < playerList.size(); i++) {
                Player player = playerList.get(i);
                if (player.getId() == id) {
                    continue;
                }
                if (player.getServerKey() == key) {
                    sendList.add(player.getId());
                }
            }
            if (sendList.size() > 0) {
                MsgBean msgBean = new MsgBean();
                msgBean.setCmd(cmd);
                msgBean.setSubCmd(MsgCmdConstant.MSG_BROAD_CASE_GROUP);
                msgBean.setArrayData(sendList);
                msgBean.setData(data);
                SendSocketManager.getInstance().getSocket(key).pushSendMsg(msgBean);
            }
        }
    }

    public static void sendBroadCase(int cmd, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        MsgBean msgBean = new MsgBean();
        msgBean.setSubCmd(MsgCmdConstant.MSG_BROAD_CASE);
        msgBean.setCmd(cmd);
        msgBean.setData(data);

        Map<Byte, Send> sendMap = SendSocketManager.getInstance().getSendMap();
        for (Map.Entry<Byte, Send> entry : sendMap.entrySet()) {
            Send value = entry.getValue();
            value.pushSendMsg(msgBean);
        }
    }
}
