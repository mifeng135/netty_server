package core.netty.tcp;

import core.Constants;
import core.group.MessageGroup;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class TcpConnectionHandler extends ChannelInboundHandlerAdapter {


    private static Logger logger = LoggerFactory.getLogger(TcpConnectionHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        TransferMsg transferMsg = (TransferMsg) msg;
        transferMsg.setContext(ctx);
        MessageGroup.getInstance().pushMessage(transferMsg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int playerIndex = ctx.channel().attr(Constants.PLAYER_INDEX).get();
        logger.info("connect socket close playerIndex = {}", playerIndex);
        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(() -> {
            TcpConnection connection = ctx.channel().attr(Constants.TCP).get();
            String ip = ctx.channel().attr(Constants.CONNECT_IP).get();
            int port = ctx.channel().attr(Constants.PORT).get();
            connection.connect(ip, port);
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
