package com.game.server.util;

import core.manager.HttpConnectManager;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;

public class HttpUtil {

    public static void sendMsg(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        Channel channel = HttpConnectManager.removeConnect(playerIndex);
        if (channel == null || !channel.isActive()) {
            return;
        }
        ByteBuf buf = Unpooled.buffer(data.length + 6);
        buf.writeInt(msgId);
        buf.writeShort(MSG_RESULT_SUCCESS);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendErrorMsg(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        Channel channel = HttpConnectManager.removeConnect(playerIndex);
        if (channel == null || !channel.isActive()) {
            return;
        }
        ByteBuf buf = Unpooled.buffer(data.length + 6);
        buf.writeInt(msgId);
        buf.writeShort(MSG_RESULT_FAIL);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
