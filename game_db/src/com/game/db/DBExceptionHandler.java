package com.game.db;

import core.exception.ExceptionHandler;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DBExceptionHandler implements ExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(DBExceptionHandler.class);

    @Override
    public void onException(Throwable throwable, TransferMsg msg) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        String exceptionStr = stringWriter.toString();
        logger.error(exceptionStr);
    }
}
