package com.game.gate.socket.login;

import com.game.gate.constant.MsgRegionConstant;
import com.game.gate.core.zero.SocketReceive;
import com.game.gate.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/27.
 */
public class ReceiveFromDB extends SocketReceive {

    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_TO_GATE_1;
    }

    @Override
    protected String getRegionString(int cmd) {
        return MsgRegionConstant.MSG_REGION_DB;
    }
}
