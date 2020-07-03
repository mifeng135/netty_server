package com.game.server.core.msg;

import io.netty.buffer.ByteBuf;

/**
 * Created by Administrator on 2020/6/8.
 */
public class MsgUtil {

    public static void writeString(ByteBuf buf, String str) {
        int length = str.length();
        buf.writeShort(length);
        buf.writeBytes(str.getBytes());
    }

    public static String readString(ByteBuf buf) {
        int strLength = buf.readShort();
        byte[] string = new byte[strLength];
        buf.readBytes(string);
        return new String(string);
    }
}
