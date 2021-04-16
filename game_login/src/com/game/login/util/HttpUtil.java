package com.game.login.util;

import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;

public class HttpUtil {

    public static void sendErrorMsg(ChannelHandlerContext context, int msgId, Object msg) {
        Channel channel = context.channel();
        if (channel == null || !channel.isActive()) {
            return;
        }
        byte[] data = ProtoUtil.serialize(msg);
        excuteSendMsg(channel, msgId, data, MSG_RESULT_FAIL);
    }

    public static void sendMsg(ChannelHandlerContext context, int msgId, Object msg) {
        Channel channel = context.channel();
        if (channel == null || !channel.isActive()) {
            return;
        }
        byte[] data = ProtoUtil.serialize(msg);
        excuteSendMsg(channel, msgId, data, MSG_RESULT_SUCCESS);
    }

    private static void excuteSendMsg(Channel channel, int msgId, byte[] data, int result) {
        ByteBuf buf = Unpooled.buffer(data.length + 6);
        buf.writeInt(msgId);
        buf.writeShort(result);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
