package com.game.login.util;

import com.alibaba.fastjson.JSON;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;


public class HttpJsonUtil {

    public static void sendStringMsg(ChannelHandlerContext context, Object msg) {
        Channel channel = context.channel();
        if (channel == null || !channel.isActive()) {
            return;
        }
        executeSendMsg(channel, msg);
    }

    private static void executeSendMsg(Channel channel, Object msg) {
        String string = JSON.toJSONString(msg);
        ByteBuf buf = Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
