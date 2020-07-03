package com.game.server.socket;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.zero.SocketReceive;
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
        if (cmd > MsgCmdConstant.MSG_CMD_GAME_SUB_GAME_BEGIN && cmd < MsgCmdConstant.MSG_CMD_GAME_SUB_GAME_END) {
            return MsgRegionConstant.MSG_REGION_GAME;
        }
        return MsgRegionConstant.MSG_REGION_ROOM;
    }
}
