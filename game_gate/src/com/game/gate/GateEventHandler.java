package com.game.gate;

import com.game.gate.util.TcpUtil;
import core.annotation.ctrl.CtrlA;
import core.group.EventHandler;
import core.manager.LocalRouterSocketManager;
import core.msg.TransferMsg;

import java.util.List;

import static core.Constants.MSG_TYPE_REMOTE;

public class GateEventHandler implements EventHandler {

    @Override
    public void onEvent(TransferMsg transferMsg) {
        CtrlA ctrlAnnotation = CtrlA.getInstance();
        int msgId = transferMsg.getHeaderProto().getMsgId();
        if (!ctrlAnnotation.getMethodMap().containsKey(msgId)) {
            transmitMsg(transferMsg);
            return;
        }
        CtrlA.getInstance().invokeMethod(transferMsg);
    }

    private void transmitMsg(TransferMsg transferMsg) {
        int msgType = transferMsg.getHeaderProto().getMsdType();
        if (msgType == MSG_TYPE_REMOTE) {
            sendRemoteMsg(transferMsg);
        } else {
            LocalRouterSocketManager.getInstance().sendRouterMsg(transferMsg);
        }
    }

    /**
     * 向客户端发送消息
     *
     * @param msg
     */
    private void sendRemoteMsg(TransferMsg msg) {
        List noticeList = msg.getHeaderProto().getNoticeList();
        int msgId = msg.getHeaderProto().getMsgId();
        if (noticeList != null) {
            TcpUtil.sendToClientWithList(noticeList, msgId, msg.getData());
        } else if (msg.getHeaderProto().isBroadcast()) {
            TcpUtil.sendAllClient(msgId, msg.getData());
        } else {
            if (msg.getHeaderProto().isSuccess()) {
                TcpUtil.sendToClient(msg.getHeaderProto().getPlayerIndex(), msgId, msg.getData());
            } else {
                TcpUtil.sendToClientError(msg.getHeaderProto().getPlayerIndex(), msgId, msg.getData());
            }
        }
    }
}
