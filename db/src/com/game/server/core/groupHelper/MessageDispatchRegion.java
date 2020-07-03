package com.game.server.core.groupHelper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageDispatchRegion {

    private ConcurrentHashMap<String, MessageGroup> mMessageRegion = new ConcurrentHashMap();

    private static class DefaultInstance {
        static final MessageDispatchRegion INSTANCE = new MessageDispatchRegion();
    }

    public static MessageDispatchRegion getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private MessageDispatchRegion() {

    }
    public void addRegion(String regionTag, MessageGroup messageGroup) {
        mMessageRegion.putIfAbsent(regionTag, messageGroup);
    }

    public MessageGroup getMessageGroupByTag(String tag) {
        return mMessageRegion.get(tag);
    }
}
