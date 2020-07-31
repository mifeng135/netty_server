package com.game.server.core.netty;

import com.game.server.core.config.Configs;
import com.game.server.core.msg.MsgBean;
import com.game.server.eventGroup.login.EventDispatch;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
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

    private static AtomicInteger fd = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        int socketFd = fd.incrementAndGet();
        if (socketFd >= Integer.MAX_VALUE) {
            fd.compareAndSet(Integer.MAX_VALUE, 0);
        }
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            HttpMethod method = request.method();
            if (HttpMethod.POST.equals(method)) {
                ByteBuf buf = request.content();
                MsgBean msgBean = new MsgBean();
                msgBean.serializeMsgLogin(buf);
                msgBean.setFd(socketFd);
                msgBean.setContext(ctx);
                EventDispatch.getInstance().pushMsg(msgBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Configs.REMOTE_ADDRESS).setIfAbsent(ip);
    }

    private void send(ChannelHandlerContext ctx, ByteBuf buf) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
