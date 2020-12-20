package core.groupHelper;

import core.proto.TransferMsg;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageThreadTask extends Thread {

    private static Logger logger = Logger.getLogger(MessageThreadTask.class);

    private final MessageGroup mMessageGroup;
    private final EventHandler mProcessEventHandler;
    private final String name;

    public MessageThreadTask(MessageGroup messageGroup, EventHandler processEventHandler, String taskName) {
        name = taskName;
        mMessageGroup = messageGroup;
        mProcessEventHandler = processEventHandler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; i++) {
                    TransferMsg transferMsg = mMessageGroup.popMessageWithTag(name);
                    if (transferMsg == null) {
                        break;
                    }
                    if (mProcessEventHandler != null) {
                        mProcessEventHandler.onEvent(transferMsg);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
