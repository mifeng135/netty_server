package com.game.server.core.netty;

import com.game.server.core.config.Configs;
import com.game.server.core.groupHelper.MessageDispatchRegion;
import com.game.server.core.groupHelper.MessageGroup;
import com.game.server.core.msg.MsgBean;
import com.game.server.serverConfig.ServerConfig;
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
                dispatch(msgBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(MsgBean msgBean) {
        String regionString = ServerConfig.REGION_LOGIN;
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + msgBean.getFd() % count;
            messageGroup.pushMessageWithTag(section, msgBean);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Configs.REMOTE_ADDRESS).setIfAbsent(ip);
    }
}
