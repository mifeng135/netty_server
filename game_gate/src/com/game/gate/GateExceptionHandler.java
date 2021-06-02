package com.game.gate;

import com.game.gate.util.TcpUtil;
import core.exception.ExceptionHandler;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.msg.ExceptionMsg;

import java.io.PrintWriter;
import java.io.StringWriter;

import static core.msg.SysMsgConstants.MSG_SYSTEM_EXCEPTION_PUSH;


public class GateExceptionHandler implements ExceptionHandler {


    private static Logger logger = LoggerFactory.getLogger(CustomEventHandler.class);


    @Override
    public void onException(Throwable throwable, TransferMsg msg) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        String exceptionStr = stringWriter.toString();
        ExceptionMsg exceptionMsg = new ExceptionMsg();
        exceptionMsg.setException(exceptionStr);
        TcpUtil.sendToClient(msg.getHeaderProto().getPlayerIndex(), MSG_SYSTEM_EXCEPTION_PUSH, exceptionMsg);
        logger.error(exceptionStr);
    }
}
