package com.game.gate;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.game.gate.util.TcpUtil;
import core.annotation.ctrl.CtrlA;
import core.group.EventHandler;
import core.manager.LocalRouterSocketManager;
import core.msg.TransferMsg;

import java.lang.reflect.Method;
import java.util.List;

import static core.Constants.MSG_TYPE_REMOTE;

public class CustomEventHandler implements EventHandler {

    @Override
    public void onEvent(TransferMsg transferMsg) {
        CtrlA ctrlAnnotation = CtrlA.getInstance();
        int msgId = transferMsg.getHeaderProto().getMsgId();
        Method method = ctrlAnnotation.getMethodMap().get(msgId);
        if (method == null) {
            transmitMsg(transferMsg);
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = ctrlAnnotation.getClassMap().get(declaringClassName);
        MethodAccess methodAccess = ctrlAnnotation.getMethodAccessMap().get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, transferMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
