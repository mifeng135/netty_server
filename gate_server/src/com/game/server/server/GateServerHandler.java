package com.game.server.server;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.config.Configs;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.netty.ServerHandler;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.proto.ProtoLinkStateS;
import com.game.server.proto.ProtoLoginR;
import com.game.server.socket.db.SendToDB;
import com.game.server.socket.game.SendToGame;
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
                ProtoLoginR protoLoginR = ProtoUtil.deserializer(msgBean.getData(), ProtoLoginR.class);
                int playerId = protoLoginR.getPlayerId();
                context.channel().attr(Configs.PLAYER_INDEX).setIfAbsent(playerId);

                Channel oldChannel = ConnectionManager.getChannelById(playerId);
                if (oldChannel != null) {
                    ConnectionManager.mfdChannelGroup.remove(playerId);
                    channelCloseEvent(context);
                }
                ConnectionManager.mfdChannelGroup.putIfAbsent(playerId, context.channel().id());
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
        channelCloseEvent(context);
    }

    private void channelCloseEvent(ChannelHandlerContext context) {
        int id = context.channel().attr(Configs.PLAYER_INDEX).get();

        ProtoLinkStateS protoLinkStateS = new ProtoLinkStateS();
        protoLinkStateS.setState(0);
        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setData(ProtoUtil.serialize(protoLinkStateS));
        msgBean.setCmd(MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_S);
        SendToGame.getInstance().pushSendMsg(msgBean);
    }

    private void samePlayerLogin(Channel channel) {

        MsgBean bean = new MsgBean();
//        bean.setCmd();
//
//        ConnectionManager.send2Client(channel,);
    }
}
