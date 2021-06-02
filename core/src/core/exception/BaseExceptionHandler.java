package core.exception;

import core.msg.ExceptionMsg;
import core.msg.TransferMsg;
import core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import static core.msg.SysMsgConstants.MSG_SYSTEM_EXCEPTION_PUSH;

public class BaseExceptionHandler implements ExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @Override
    public void onException(Throwable throwable, TransferMsg msg) {
        String exceptionStr = StringUtil.throwableToStr(throwable, msg);
        ExceptionMsg exceptionMsg = new ExceptionMsg();
        exceptionMsg.setException(exceptionStr);
        msg.getHeaderProto().setMsgId(MSG_SYSTEM_EXCEPTION_PUSH);
        logger.error(exceptionStr);
    }
}
