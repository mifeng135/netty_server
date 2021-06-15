package com.game.db.util;

import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;

public class MsgUtil {

    public static void sendMsg(TransferMsg transferMsg, Object msgObj) {
        ByteBuf byteBuf = ProtoUtil.encodeDBHttpMsg(transferMsg, msgObj);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        transferMsg.getContext().channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
