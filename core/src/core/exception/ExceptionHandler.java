package core.exception;

import core.msg.TransferMsg;

public interface ExceptionHandler {
    void onException(Throwable throwable, TransferMsg msg);
}
