package core.netty.http;


import core.Constants;
import core.group.MessageGroup;
import core.msg.HeaderProto;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
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
        int httpIndex = atomicInteger.incrementAndGet();
        if (httpIndex >= Integer.MAX_VALUE) {
            atomicInteger.compareAndSet(Integer.MAX_VALUE, 0);
        }
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            HttpMethod method = request.method();
            if (HttpMethod.POST.equals(method)) {
                ByteBuf buf = request.content();

                int headerLen = buf.readShort();
                int bodyLen = buf.readShort();

                byte[] headerData = new byte[headerLen];
                buf.readBytes(headerData);

                byte[] bodyData = new byte[bodyLen];
                buf.readBytes(bodyData);

                HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);
                int playerIndex = headerProto.getPlayerIndex() == 0 ? httpIndex : headerProto.getPlayerIndex();
                headerProto.setPlayerIndex(playerIndex);
                TransferMsg transferMsg = new TransferMsg();
                transferMsg.setHeaderProto(headerProto);
                transferMsg.setData(bodyData);
                transferMsg.setContext(ctx);
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
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }
}
