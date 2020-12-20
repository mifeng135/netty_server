package com.game.server.server;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.proto.ProtoLoginR;
import core.Configs;
import core.manager.WebSocketConnectionManager;
import core.netty.WebSocketHandler;
import core.proto.ProtoUtil;
import core.proto.TransferMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/24.
 */

@ChannelHandler.Sharable
public class GateServerHandler extends WebSocketHandler {

    private static Logger logger = Logger.getLogger(WebSocketHandler.class);

    @Override
    public boolean swallowDispatchMsg(ChannelHandlerContext context, TransferMsg msgBean) {
        if (msgBean.getMsgId() == MsgCmdConstant.MSG_CMD_LOGIN_TO_GATE_R) {
            try {
                ProtoLoginR protoLoginR = ProtoUtil.deserializer(msgBean.getData(), ProtoLoginR.class);
                int playerIndex = protoLoginR.getPlayerId();
                context.channel().attr(Configs.PLAYER_INDEX).setIfAbsent(playerIndex);

                Channel oldChannel = WebSocketConnectionManager.getChannelByPlayerIndex(playerIndex);
                if (oldChannel != null) {
                    samePlayerLogin(oldChannel);
                    WebSocketConnectionManager.mfdChannelGroup.remove(playerIndex);
                }
                WebSocketConnectionManager.mfdChannelGroup.putIfAbsent(playerIndex, context.channel().id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (msgBean.getMsgId() == MsgCmdConstant.MSG_HEART_BEAT_R) {
            logger.info("client heart beat");
            return true;
        }
        return false;
    }

    @Override
    public void dispatchMsg(TransferMsg transferMsg) {
//        if (transferMsg.getMsgId() > MsgCmdConstant.MSG_CMD_DB_BEGIN && transferMsg.getMsgId() < MsgCmdConstant.MSG_CMD_DB_END) {
//            SendSocketManager.getInstance().getSocket(transferMsg.getServerKey()).pushSendMsg(transferMsg);
//        } else if (transferMsg.getMsgId() > MsgCmdConstant.MSG_CMD_GAME_BEGIN && transferMsg.getMsgId() < MsgCmdConstant.MSG_CMD_GAME_END) {
//            SendSocketManager.getInstance().getSocket(transferMsg.getServerKey()).pushSendMsg(transferMsg);
//        }
    }

    @Override
    public void channelClose(ChannelHandlerContext context) {
        channelCloseEvent(context);
    }

    private void channelCloseEvent(ChannelHandlerContext context) {
        int id = context.channel().attr(Configs.PLAYER_INDEX).get();
//        ProtoLinkStateS protoLinkStateS = new ProtoLinkStateS();
//        protoLinkStateS.setState(0);
//        MsgBean msgBean = new MsgBean();
//        msgBean.setId(id);
//        msgBean.setData(ProtoUtil.serialize(protoLinkStateS));
//        msgBean.setCmd(MsgCmdConstant.MSG_CMD_SERVER_LINK_STATE_S);
//        SendSocketManager.getInstance().getSocket(ServerConfig.GATE_GAME_SERVER_KEY).pushSendMsg(msgBean);
    }

    private void samePlayerLogin(Channel channel) {
        int id = channel.attr(Configs.PLAYER_INDEX).get();
//        MsgBean msgBean = new MsgBean();
//        msgBean.setId(id);
//        msgBean.setCmd(MsgCmdConstant.MSG_CMD_REPLACE_ACCOUNT_S);
//        msgBean.setData(new byte[0]);
//        WebSocketConnectionManager.send2Client(channel, msgBean.packClientMsg());
    }
}
