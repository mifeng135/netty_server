package core.netty;


import core.Configs;
import core.manager.HttpConnectManager;
import core.proto.TransferMsg;
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
public abstract class HttpHandler extends ChannelInboundHandlerAdapter {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

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
                int index = buf.readInt();
                int msgId = buf.readInt();
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                TransferMsg transferMsg = new TransferMsg();
                transferMsg.setMsgId(msgId);
                transferMsg.setPlayerIndex(playerIndex);
                transferMsg.setData(data);
                HttpConnectManager.putConnect(playerIndex, ctx.channel());
                dispatch(transferMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void dispatch(TransferMsg msgBean);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Configs.REMOTE_ADDRESS).setIfAbsent(ip);
    }
}
