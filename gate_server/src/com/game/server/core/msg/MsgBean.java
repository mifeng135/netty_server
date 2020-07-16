package com.game.server.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Administrator on 2020/6/19.
 */
public class MsgBean {
    private int id;
    private int cmd;
    private short subCmd;
    private int dataLength;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        dataLength = data.length;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public short getSubCmd() {
        return subCmd;
    }

    public void setSubCmd(short subCmd) {
        this.subCmd = subCmd;
    }

    public ByteBuf toByteBuf() {
        ByteBuf buf = Unpooled.buffer(14 + dataLength);
        buf.writeInt(id);
        buf.writeInt(cmd);
        buf.writeShort(subCmd);
        buf.writeInt(dataLength);
        buf.writeBytes(data);
        return buf;
    }

    public void serializeMsg(byte[] msgData) {
        ByteBuf buf = Unpooled.wrappedBuffer(msgData);
        id = buf.readInt();
        cmd = buf.readInt();
        subCmd = buf.readShort();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

    public ByteBuf packClientMsg() {
        ByteBuf buf = Unpooled.buffer(8 + dataLength);
        buf.writeInt(cmd);
        buf.writeInt(dataLength);
        buf.writeBytes(data);
        return buf;
    }
}
