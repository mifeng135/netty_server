package com.game.db.socket;

import com.game.db.constant.MsgRegionConstant;
import com.game.db.core.zero.SocketRecv;
import com.game.db.serverConfig.ServerConfig;

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
