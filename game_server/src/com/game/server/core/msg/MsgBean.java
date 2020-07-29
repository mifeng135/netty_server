package com.game.server.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/6/19.
 */
public class MsgBean {
    private int id;
    private int cmd;
    private short subCmd;
    private short arrayLen;
    private List<Integer> arrayData;
    private int dataLength;
    private byte[] data;
    private byte serverKey;

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

    public List<Integer> getArrayData() {
        return arrayData;
    }

    public void setArrayData(List<Integer> arrayData) {
        arrayLen = (short) arrayData.size();
        this.arrayData = arrayData;
    }

    public byte getServerKey() {
        return serverKey;
    }

    public void setServerKey(byte serverKey) {
        this.serverKey = serverKey;
    }
    public ByteBuf toByteBuf() {
        ByteBuf buf = Unpooled.buffer(16 + arrayLen * 4 + dataLength);
        buf.writeInt(id);
        buf.writeInt(cmd);
        buf.writeShort(subCmd);
        buf.writeShort(arrayLen);
        writeArray(buf);
        buf.writeInt(dataLength);
        buf.writeBytes(data);
        return buf;
    }

    public void serializeMsg(byte[] msgData) {
        ByteBuf buf = Unpooled.wrappedBuffer(msgData);
        id = buf.readInt();
        cmd = buf.readInt();
        subCmd = buf.readShort();
        arrayLen = buf.readShort();
        readArray(buf);
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

    private void writeArray(ByteBuf buf) {
        if (arrayLen > 0) {
            for (int i = 0; i < arrayLen; i++) {
                buf.writeInt(arrayData.get(i));
            }
        }
    }

    private void readArray(ByteBuf buf) {
        if (arrayLen > 0) {
            arrayData = new ArrayList<>();
            for (int i = 0; i < arrayLen; i++) {
                arrayData.add(buf.readInt());
            }
        }
    }
}
