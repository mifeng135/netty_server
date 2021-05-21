package com.game.login.util;

import com.alibaba.fastjson.JSON;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageSizeEstimator;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static core.Constants.*;

public class HttpUtil {

    public static void sendErrorMsg(ChannelHandlerContext context, int msgId, Object msg) {
        Channel channel = context.channel();
        if (channel == null || !channel.isActive()) {
            return;
        }
        byte[] data = ProtoUtil.serialize(msg);
        executeSendMsg(channel, msgId, data, MSG_RESULT_FAIL);
    }


    public static void sendMsg(TransferMsg msg, int msgId, Object msgObj) {
        Channel channel = msg.getContext().channel();
        if (channel == null || !channel.isActive()) {
            return;
        }
        int type = msg.getMsgType();
        if (type == HTTP_DECODER_TYPE_JSON) {
            executeSendMsg(channel, msgObj, msg.getHttpUrl());
            return;
        }
        byte[] data = ProtoUtil.serialize(msgObj);
        executeSendMsg(channel, msgId, data, MSG_RESULT_SUCCESS);
    }

    private static void executeSendMsg(Channel channel, int msgId, byte[] data, int result) {
        ByteBuf buf = Unpooled.buffer(data.length + 6);
        buf.writeInt(msgId);
        buf.writeShort(result);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void executeSendMsg(Channel channel, Object msgObj, String path) {
        String string = JSON.toJSONString(msgObj);
        ByteBuf buf = Unpooled.copiedBuffer(string, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HTTP_HEADER_PATH, path);
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
