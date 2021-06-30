package core.netty.tcp;

import core.Constants;
import core.group.MessageGroup;
import core.msg.SysMsgConstants;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static core.Constants.LOCAL_SOCKET_RANGE;
import static core.msg.SysMsgConstants.MSG_LOCAL_SOCKET_CLOSE_PUSH;
import static core.msg.SysMsgConstants.MSG_REMOTE_SOCKET_CLOSE_PUSH;

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
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            ctx.close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Constants.REMOTE_ADDRESS).setIfAbsent(ip);
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
        if (playerIndex < LOCAL_SOCKET_RANGE) {
            transferMsg.setHeaderProto(ProtoUtil.initHeaderProto(MSG_LOCAL_SOCKET_CLOSE_PUSH, playerIndex));
        } else {
            transferMsg.setHeaderProto(ProtoUtil.initHeaderProto(MSG_REMOTE_SOCKET_CLOSE_PUSH, playerIndex));
        }
        MessageGroup.getInstance().pushMessage(transferMsg);
    }
}
