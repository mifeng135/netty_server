package core.netty.tcp;

import core.Constants;
import core.group.MessageGroup;
import core.manager.LocalSocketManager;
import core.manager.SocketManager;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.Constants.LOCAL_SOCKET_RANGE;


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
            channelClose(ctx);
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
        ctx.close();
    }

    protected void channelClose(ChannelHandlerContext context) {
        int playerIndex = context.channel().attr(Constants.PLAYER_INDEX).get();
        if (playerIndex < LOCAL_SOCKET_RANGE) {
            LocalSocketManager.getInstance().removeChannel(context.channel());
        } else {
            SocketManager.getInstance().removeChannel(context.channel());
        }
    }
}
