package com.game.server.core.groupHelper;

import com.game.server.core.msg.MsgBean;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageThreadTask extends Thread {

    private static Logger logger = Logger.getLogger(MessageThreadTask.class);

    private final MessageGroup mMessageGroup;
    private final EventHandler mProcessEventHandler;

    public MessageThreadTask(MessageGroup messageGroup, EventHandler processEventHandler) {
        mMessageGroup = messageGroup;
        mProcessEventHandler = processEventHandler;
    }


    @Override
    public void run() {
        while (true) {
            String name = getName();
            try {
                for (int i = 0; i < 100; i++) {
                    MsgBean msgBean = mMessageGroup.popMessageWithTag(name);
                    if (msgBean == null) {
                        break;
                    }
                    if (mProcessEventHandler != null) {
                        mProcessEventHandler.onEvent(msgBean);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
