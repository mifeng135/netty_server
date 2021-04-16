package com.game.db.util;

import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import protocal.local.base.HeaderProto;

public class MsgUtil {

    public static void sendMsg(ChannelHandlerContext context, HeaderProto headerProto, Object msg) {

        byte[] headerData = ProtoUtil.serialize(headerProto);
        byte[] bodyData = ProtoUtil.serialize(msg);
        ByteBuf buf = Unpooled.buffer(4 + headerData.length + bodyData.length);
        buf.writeShort(headerData.length);
        buf.writeShort(bodyData.length);
        buf.writeBytes(headerData);
        buf.writeBytes(bodyData);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        context.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
