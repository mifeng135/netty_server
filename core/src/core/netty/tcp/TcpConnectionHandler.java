package core.netty.tcp;

import core.Constants;
import core.group.EventThreadGroup;
import core.group.MessageGroup;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

import static core.Constants.SOCKET_INDEX;
import static protocol.MsgConstant.MSG_TCP_RSP;

@ChannelHandler.Sharable
public class TcpConnectionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int socketIndex = ctx.channel().attr(SOCKET_INDEX).get();
        TransferMsg transferMsg = (TransferMsg) msg;
        transferMsg.setSocketIndex(socketIndex);
        transferMsg.setContext(ctx);
        MessageGroup.getInstance().pushMessage(transferMsg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                TcpConnection connection = ctx.channel().attr(Constants.TCP).get();
                String ip = ctx.channel().attr(Constants.CONNECT_IP).get();
                int port = ctx.channel().attr(Constants.PORT).get();
                connection.connect(ip, port);
            }
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public void dispatchMsg(TransferMsg msgBean) {
        MessageGroup.getInstance().pushMessage(msgBean);
    }

    public boolean swallowDispatchMsg(TransferMsg msg) {
        if (msg.getMsgId() == MSG_TCP_RSP) {
            return true;
        }
        return false;
    }
}
