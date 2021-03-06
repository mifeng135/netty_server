package com.game.server;

import com.esotericsoftware.reflectasm.MethodAccess;
import core.annotation.CtrlAnnotation;
import core.group.EventHandler;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static core.Constants.MSG_TYPE_REMOTE;
import static protocol.MsgConstant.MSG_GATE_SEND_LOCAL;
import static protocol.MsgConstant.MSG_GATE_SEND_REMOTE;

public class CustomEventHandler implements EventHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomEventHandler.class);

    @Override
    public void onEvent(TransferMsg transferMsg) {
        CtrlAnnotation ctrlAnnotation = CtrlAnnotation.getInstance();
        int msgId = transferMsg.getHeaderProto().getMsgId();
        int msgType = transferMsg.getHeaderProto().getMsdType();
        Method method = ctrlAnnotation.getMethodMap().get(msgId);
        if (method == null) {
            if (msgType == MSG_TYPE_REMOTE) {
                method = ctrlAnnotation.getMethodMap().get(MSG_GATE_SEND_LOCAL);
            } else {
                method = ctrlAnnotation.getMethodMap().get(MSG_GATE_SEND_REMOTE);
            }
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = ctrlAnnotation.getClassMap().get(declaringClassName);
        MethodAccess methodAccess = ctrlAnnotation.getMethodAccessMap().get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, transferMsg, transferMsg.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
