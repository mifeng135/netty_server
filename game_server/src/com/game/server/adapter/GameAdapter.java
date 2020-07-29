package com.game.server.adapter;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.zero.ReceiveAdapter;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;

/**
 * Created by Administrator on 2020/7/29.
 */
public class GameAdapter implements ReceiveAdapter {
    @Override
    public int getSectionId(MsgBean msgBean) {
        int cmd = msgBean.getCmd();
        switch (cmd) {
            case MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R:
            case MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R:
            case MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R:
            case MsgCmdConstant.MSG_CMD_GAME_READY_R:
            case MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_R:
                return msgBean.getId();
        }
        Player player = PlayerManager.getInstance().getPlayer(msgBean.getId());
        return player.getRoomId();
    }

    @Override
    public String getRegionString(MsgBean msgBean) {

        int cmd = msgBean.getCmd();
        String regionString = MsgRegionConstant.MSG_REGION_GAME;
        switch (cmd) {
            case MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R:
            case MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R:
            case MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_R:
            case MsgCmdConstant.MSG_CMD_GAME_READY_R:
            case MsgCmdConstant.MSG_CMD_GAME_ROOM_LIST_R:
                regionString = MsgRegionConstant.MSG_REGION_ROOM;
                break;
        }
        return regionString;
    }
}
