package com.game.game.core.groupHelper;

import com.game.game.core.msg.MsgBean;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageGroup {

    private ConcurrentHashMap<String,ConcurrentLinkedQueue<MsgBean>> mMessageMap = new ConcurrentHashMap<>();
    private final int mCount;
    private final String mPrefix;

    public MessageGroup(int count,String prefix) {
        this.mCount = count;
        this.mPrefix = prefix;
    }

    public int getCount() {
        return mCount;
    }
    public String getPrefix() {
        return mPrefix;
    }
    public ConcurrentLinkedQueue getQueueByTag(String tag) {
        return mMessageMap.get(tag);
    }

    public void createQueueByTag(String tag) {
        mMessageMap.putIfAbsent(tag,new ConcurrentLinkedQueue());
    }

    public void pushMessageWithTag(String tag, MsgBean buf) {
        getQueueByTag(tag).offer(buf);
    }

    public MsgBean popMessageWithTag(String tag) {
        return (MsgBean) getQueueByTag(tag).poll();
    }
}
