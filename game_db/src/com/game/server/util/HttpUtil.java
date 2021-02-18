package com.game.server.util;

import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class HttpUtil {

    public static void sendMsg(ChannelHandlerContext context, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        ByteBuf buf = Unpooled.copiedBuffer(data);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        context.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
