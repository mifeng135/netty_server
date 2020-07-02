package com.game.login.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * Created by Administrator on 2020/6/8.
 */
public class MsgUtil {


    public static void sendMsg(ChannelHandlerContext context, int cmd, byte[] data) {

        ByteBuf buf = Unpooled.buffer(data.length + 8);
        buf.writeInt(cmd);
        buf.writeInt(data.length);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
