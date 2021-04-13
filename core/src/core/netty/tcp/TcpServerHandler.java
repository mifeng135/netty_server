package core.netty.tcp;

import core.Constants;
import core.group.MessageGroup;
import core.manager.LocalSocketManager;
import core.manager.RemoteSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static protocal.MsgConstant.MSG_CLOSE_SOCKET_REQ;


/**
 * Created by Administrator on 2020/12/19.
 */
@ChannelHandler.Sharable
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        TransferMsg transferMsg = (TransferMsg) msg;
        transferMsg.setContext(ctx);
        MessageGroup.getInstance().pushMessage(transferMsg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channelClose(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }

    protected void channelClose(ChannelHandlerContext context) {
        int playerIndex = context.channel().attr(Constants.PLAYER_INDEX).get();
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setContext(context);
        transferMsg.setHeaderProto(ProtoUtil.initHeaderProto(MSG_CLOSE_SOCKET_REQ, playerIndex));
        MessageGroup.getInstance().pushMessage(transferMsg);
    }
}
