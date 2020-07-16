package com.game.server.core.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2020/6/19.
 */
public class MsgBean {
    private int id;
    private int cmd;
    private short subCmd;
    private int dataLength;
    private byte[] data;


    /**
     * 下面两个是专门给登录定制的
     */
    private int fd;
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

    public int getId() {
        return id;
    }

    public void setId(int playerIndex) {
        this.id = playerIndex;
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

    /**
     * 这个是专门给login定义的方法
     * @param buf
     */
    public void serializeMsgLogin(ByteBuf buf) {
        cmd = buf.readInt();
        dataLength = buf.readInt();
        data = new byte[dataLength];
        buf.readBytes(data);
        buf.release();
    }
}
