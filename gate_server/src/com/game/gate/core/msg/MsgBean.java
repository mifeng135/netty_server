package com.game.gate.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Administrator on 2020/6/19.
 */
public class MsgBean {
    private int id;
    private int cmd;
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

    public ByteBuf toByteBuf() {
        ByteBuf buf = Unpooled.buffer(12 + dataLength);
        buf.writeInt(id);
        buf.writeInt(cmd);
        buf.writeInt(dataLength);
        buf.writeBytes(data);
        return buf;
    }

    public void serializeMsg(ByteBuf buf) {
        id = buf.readInt();
        cmd = buf.readInt();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

    public void serializeMsg(byte[] msgData) {
        ByteBuf buf = Unpooled.wrappedBuffer(msgData);
        id = buf.readInt();
        cmd = buf.readInt();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

    public ByteBuf packClienMsg() {
        ByteBuf buf = Unpooled.buffer(8 + dataLength);
        buf.writeInt(cmd);
        buf.writeInt(dataLength);
        buf.writeBytes(data);
        return buf;
    }

}
