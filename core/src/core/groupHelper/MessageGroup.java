package core.groupHelper;



import core.proto.TransferMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageGroup {

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<TransferMsg>> mMessageMap = new ConcurrentHashMap<>();
    private final int mCount;
    private final String mPrefix;

    public MessageGroup(int count, String prefix) {
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
        mMessageMap.putIfAbsent(tag, new ConcurrentLinkedQueue());
    }

    public void pushMessageWithTag(String tag, TransferMsg buf) {
        getQueueByTag(tag).offer(buf);
    }

    public TransferMsg popMessageWithTag(String tag) {
        return (TransferMsg) getQueueByTag(tag).poll();
    }
}
