package core.group;

import core.annotation.ctrl.CtrlA;
import core.msg.TransferMsg;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageThreadTask extends Thread {

    private final MessageGroup mMessageGroup;
    private final EventHandler mProcessEventHandler;
    private final int tag;


    public MessageThreadTask(MessageGroup messageGroup, EventHandler processEventHandler, int taskName) {
        tag = taskName;
        mMessageGroup = messageGroup;
        mProcessEventHandler = processEventHandler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 500; i++) {
                    TransferMsg transferMsg = mMessageGroup.popMessage(tag);
                    if (transferMsg == null) {
                        break;
                    }
                    if (mProcessEventHandler != null) {
                        mProcessEventHandler.onEvent(transferMsg);
                    } else {
                        CtrlA.getInstance().invokeMethod(transferMsg);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
