package com.game.login;

import com.game.login.util.HttpUtil;
import core.exception.ExceptionHandler;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.ExceptionMsg;

import java.io.PrintWriter;
import java.io.StringWriter;

import static MsgConstant.MSG_SYSTEM_EXCEPTION_PUSH;

public class LoginExceptionHandler implements ExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(LoginExceptionHandler.class);

    @Override
    public void onException(Throwable throwable, TransferMsg msg) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        String exceptionStr = stringWriter.toString();
        ExceptionMsg exceptionMsg = new ExceptionMsg();
        exceptionMsg.setException(exceptionStr);
        HttpUtil.sendMsg(msg, MSG_SYSTEM_EXCEPTION_PUSH, exceptionMsg);
        logger.error(exceptionStr);
    }
}
