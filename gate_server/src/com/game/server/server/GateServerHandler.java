package com.game.server.server;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.config.Configs;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.netty.ServerHandler;
import com.game.server.proto.LinkSynMsg;
import com.game.server.proto.LoginMsg;
import com.game.server.socket.login.SendToDB;
import com.game.server.socket.login.SendToGame;
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
        if (msgBean.getCmd() > MsgCmdConstant.MSG_CMD_DB_BEGIN && msgBean.getCmd() < MsgCmdConstant.MSG_CMD_DB_END) {
            SendToDB.getInstance().pushSendMsg(msgBean);
        } else if (msgBean.getCmd() > MsgCmdConstant.MSG_CMD_GAME_BEGIN && msgBean.getCmd() < MsgCmdConstant.MSG_CMD_GAME_END) {
            SendToGame.getInstance().pushSendMsg(msgBean);
        }
    }

    @Override
    public void channelClose(ChannelHandlerContext context) {
        int id = context.channel().attr(Configs.PLAYER_INDEX).get();
        LinkSynMsg.linkState.Builder linkState = LinkSynMsg.linkState.newBuilder();
        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setData(linkState.build().toByteArray());
        msgBean.setCmd(MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE);
        SendToGame.getInstance().pushSendMsg(msgBean);
    }
}