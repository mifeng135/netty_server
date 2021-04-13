package com.game.server.controller;


import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;

@Ctrl
public class PlayerInfoController {

    private static Logger logger = LoggerFactory.getLogger(PlayerInfoController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_GET_SERVER_LIST_REQ)
    public void getPlayerInfo(TransferMsg msg, ChannelHandlerContext context) {
        
    }
}
