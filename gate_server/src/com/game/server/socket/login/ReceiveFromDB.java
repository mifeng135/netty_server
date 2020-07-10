package com.game.server.socket.login;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.zero.SocketReceive;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/27.
 */
public class ReceiveFromDB extends SocketReceive {

    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_FROM_DB_1;
    }

    @Override
    protected String getRegionString(int cmd) {
        return MsgRegionConstant.MSG_REGION_DB;
    }
}
