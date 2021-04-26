package com.game.login.controller;


import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.MsgConstant;

@Ctrl
public class TestController {


    @CtrlCmd(cmd = MsgConstant.MSG_GET_PLAYER_INDEX_REQ)
    public void test(TransferMsg msg) {
        msg.getContext().channel();
    }
}
