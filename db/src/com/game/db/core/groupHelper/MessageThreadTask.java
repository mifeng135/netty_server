package com.game.db.core.groupHelper;

import com.game.db.core.msg.MsgBean;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageThreadTask extends Thread {

    private static Logger logger = Logger.getLogger(MessageThreadTask.class);

    private final MessageGroup mMessageGroup;
    private final EventHandler mProcessEventHandler;
    public MessageThreadTask(MessageGroup messageGroup,EventHandler processEventHandler) {
        mMessageGroup = messageGroup;
        mProcessEventHandler = processEventHandler;
    }

    @Override
    public void run() {
        while (true) {
            String name = getName();
            try {
                MsgBean msgBean = mMessageGroup.popMessageWithTag(name);
                if (msgBean == null) {
                    Thread.sleep(100);
                    continue;
                }
                if (mProcessEventHandler != null) {
                    mProcessEventHandler.onEvent(msgBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
