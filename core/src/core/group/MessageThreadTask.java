package core.group;

import core.annotation.CtrlAnnotation;
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

    public int getTag() {
        return tag;
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
                        CtrlAnnotation.getInstance().invokeMethod(transferMsg, transferMsg.getContext());
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
