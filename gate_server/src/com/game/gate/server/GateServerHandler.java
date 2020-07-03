package com.game.gate.server;

import com.game.gate.constant.MsgCmdConstant;
import com.game.gate.core.config.Configs;
import com.game.gate.core.connect.ConnectionManager;
import com.game.gate.core.msg.MsgBean;
import com.game.gate.core.netty.ServerHandler;
import com.game.gate.proto.LoginMsg;
import com.game.gate.socket.login.SendToDB;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2020/6/24.
 */

@ChannelHandler.Sharable
public class GateServerHandler extends ServerHandler {

    @Override
    public boolean swallowDispatchMsg(ChannelHandlerContext context, MsgBean msgBean) {
        if (msgBean.getCmd() == MsgCmdConstant.MSG_CMD_LOGIN_TO_GATE_R) {
            try {
                LoginMsg.loginToGateR loginToGateR = LoginMsg.loginToGateR.parseFrom(msgBean.getData());
                int playerIndex = loginToGateR.getPlayerIndex();
                context.channel().attr(Configs.PLAYER_INDEX).setIfAbsent(playerIndex);

                Channel oldChannel = ConnectionManager.getChannelById(playerIndex);
                if (oldChannel != null) {
                    ConnectionManager.mfdChannelGroup.remove(playerIndex);
                }
                ConnectionManager.mfdChannelGroup.putIfAbsent(playerIndex, context.channel().id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void dispatchMsg(MsgBean msgBean) {
        SendToDB.getInstance().pushSendMsg(msgBean);
    }
}
