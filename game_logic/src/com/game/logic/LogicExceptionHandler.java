package com.game.logic;

import com.game.logic.util.MsgUtil;
import core.exception.ExceptionHandler;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.ExceptionMsg;

import java.io.PrintWriter;
import java.io.StringWriter;

import static MsgConstant.MSG_SYSTEM_EXCEPTION_PUSH;

public class LogicExceptionHandler implements ExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(LogicExceptionHandler.class);

    @Override
    public void onException(Throwable throwable, TransferMsg msg) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        String exceptionStr = stringWriter.toString();
        ExceptionMsg exceptionMsg = new ExceptionMsg();
        exceptionMsg.setException(exceptionStr);
        msg.getHeaderProto().setMsgId(MSG_SYSTEM_EXCEPTION_PUSH);
        MsgUtil.sendMsg(msg.getHeaderProto(), exceptionMsg);
        logger.error(exceptionStr);
    }
}
