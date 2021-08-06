package core;

import core.msg.TransferMsg;

public abstract class BaseController {

    void before(TransferMsg msg) {
        process(msg);
    }

    abstract void process(TransferMsg msg);
}
