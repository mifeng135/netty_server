package com.game.server.socket;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.zero.SocketReceive;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class ReceiveFromGate extends SocketReceive {

    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_FROM_GATE;
    }
    @Override
    protected String getRegionString(int cmd) {
        String regionString = MsgRegionConstant.MSG_REGION_GAME;

        switch (cmd) {
            case MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R:
            case MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R:
            case MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R:
            case MsgCmdConstant.MSG_CMD_GAME_READY_R:
                regionString = MsgRegionConstant.MSG_REGION_ROOM;
                break;
        }
        return regionString;
    }


    @Override
    protected boolean swallowDispatchMsg(MsgBean bean) {
        if (bean.getCmd() == MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R) {
            Player player = PlayerManager.getInstance().getPlayer(bean.getId());
            if (player == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getSectionId(MsgBean bean) {
        switch (bean.getCmd()) {
            case MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R:
            case MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R:
            case MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R:
            case MsgCmdConstant.MSG_CMD_GAME_READY_R:
                return super.getSectionId(bean);
        }
        Player player = PlayerManager.getInstance().getPlayer(bean.getId());
        return player.getRoomId();
    }

}
