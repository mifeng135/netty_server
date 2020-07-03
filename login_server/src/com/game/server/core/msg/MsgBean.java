package com.game.server.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2020/6/19.
 */
public class MsgBean {
    private int fd;
    private int playerIndex;
    private int cmd;
    private int dataLength;
    private byte[] data;

    private ChannelHandlerContext context;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }
    public ByteBuf toByteBuf() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(fd);
        buf.writeInt(playerIndex);
        buf.writeInt(cmd);
        buf.writeInt(dataLength);
        if (data != null) {
            buf.writeBytes(data);
        }
        return buf;
    }

    public void serializeMsg(ByteBuf buf) {
        cmd = buf.readInt();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

    public void serializeMsg(byte[] msgData) {
        ByteBuf buf = Unpooled.wrappedBuffer(msgData);
        playerIndex = buf.readInt();
        cmd = buf.readInt();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }

}
