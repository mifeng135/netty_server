package com.game.logic;

import core.annotation.ctrl.CtrlA;
import core.annotation.ctrl.CtrlCmd;
import core.group.EventHandler;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogicEventHandler extends AsyncCompletionHandler implements EventHandler {

    private static Logger logger = LoggerFactory.getLogger(LogicEventHandler.class);

    @Override
    public void onEvent(TransferMsg transferMsg) {
        int msgId = transferMsg.getHeaderProto().getMsgId();
        CtrlCmd ctrlCmd = CtrlA.getInstance().getCtrlCmdMap().get(msgId);
        int before = ctrlCmd.beforeCmd();
        if (before != 0) {
            transferMsg.getHeaderProto().setMsgId(before);
            transferMsg.getHeaderProto().setMsdType(msgId);
            AsyncHttp.getInstance().postAsync(transferMsg.getHeaderProto(), transferMsg.getData(), this);
            return;
        }
        CtrlA.getInstance().invokeMethod(transferMsg);
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        TransferMsg msg = ProtoUtil.decodeDBHttpMsg(response.getResponseBodyAsBytes());
        msg.getHeaderProto().setMsgId(msg.getHeaderProto().getMsdType());
        CtrlA.getInstance().invokeMethod(msg);
        return 1;
    }
}
