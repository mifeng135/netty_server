package com.game.server.util;

import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.room.Player;
import com.game.server.room.Room;
import com.game.server.socket.SendToGate;

import java.util.List;

/**
 * Created by Administrator on 2020/7/28.
 */
public class SocketUtil {

    public static void sendToGate(int cmd, Object msg, Room room) {
        byte[] data = ProtoUtil.serialize(msg);
        List<Player> playerList = room.getRoomPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            MsgBean msgBean = new MsgBean();
            msgBean.setId(player.getId());
            msgBean.setCmd(cmd);
            msgBean.setData(data);
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
    }

    public static void sendToGate(int cmd, Player player, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        MsgBean msgBean = new MsgBean();
        msgBean.setId(player.getId());
        msgBean.setCmd(cmd);
        msgBean.setData(data);
        SendToGate.getInstance().pushSendMsg(msgBean);
    }

    public static void sendToGateExcept(int cmd, Object msg, Room room, Player except) {
        byte[] data = ProtoUtil.serialize(msg);
        List<Player> playerList = room.getRoomPlayer();
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            MsgBean msgBean = new MsgBean();
            msgBean.setId(player.getId());
            msgBean.setCmd(cmd);
            msgBean.setData(data);
            SendToGate.getInstance().pushSendMsg(msgBean);
        }
    }
}
