package com.game.server.socket;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.zero.SocketRecv;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/22.
 */
public class ReceiveFromGate1 extends SocketRecv {

    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_FROM_GATE_1;
    }

    @Override
    protected String getRegionString(int cmd) {
       return MsgRegionConstant.MSG_REGION_DB;
    }
}
