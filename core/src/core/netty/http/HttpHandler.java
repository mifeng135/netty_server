package core.netty.http;


import core.Constants;
import core.group.MessageGroup;
import core.manager.HttpConnectManager;
import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2020/6/23.
 */

@ChannelHandler.Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!ctx.channel().isActive()) {
            return;
        }
        atomicInteger.incrementAndGet();
        if (atomicInteger.get() >= Integer.MAX_VALUE) {
            atomicInteger.compareAndSet(Integer.MAX_VALUE, 0);
        }

        int playerIndex = atomicInteger.get();
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            HttpMethod method = request.method();
            if (HttpMethod.POST.equals(method)) {
                ByteBuf buf = request.content();
                int msgId = buf.readInt();
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                TransferMsg transferMsg = new TransferMsg();
                transferMsg.setMsgId(msgId);
                transferMsg.setPlayerIndex(playerIndex);
                transferMsg.setData(data);
                transferMsg.setContext(ctx);
                HttpConnectManager.putConnect(playerIndex, ctx.channel());
                MessageGroup.getInstance().pushMessage(transferMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Constants.REMOTE_ADDRESS).setIfAbsent(ip);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(ctx.channel().isActive()) {
            ctx.close();
        }
    }
}
