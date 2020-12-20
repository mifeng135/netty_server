package com.game.server.server;

import core.netty.TcpHandler;
import core.proto.TransferMsg;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2020/12/19.
 */
@Sharable
public class GateTcpHandler extends TcpHandler {

    @Override
    public boolean swallowDispatchMsg(ChannelHandlerContext context, TransferMsg msgBean) {
        return false;
    }

    @Override
    public void dispatchMsg(TransferMsg msgBean) {

    }

    @Override
    public void channelClose(ChannelHandlerContext context) {

    }
}
