package com.game.db.util;

import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import protocal.HeaderProto;

public class MsgUtil {

    public static void sendMsg(TransferMsg transferMsg, Object msgObj) {
        ByteBuf byteBuf = ProtoUtil.encodeDBHttpMsg(transferMsg, msgObj);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        transferMsg.getContext().channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
