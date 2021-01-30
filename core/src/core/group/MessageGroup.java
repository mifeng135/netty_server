package core.group;


import com.conversantmedia.util.concurrent.MultithreadConcurrentQueue;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageGroup {
    private static Logger logger = LoggerFactory.getLogger(MessageGroup.class);

    private ConcurrentHashMap<Integer, MultithreadConcurrentQueue<TransferMsg>> mMessageMap = new ConcurrentHashMap();
    private int threadCount;

    private static class DefaultInstance {
        static final MessageGroup INSTANCE = new MessageGroup();
    }

    public static MessageGroup getInstance() {
        return MessageGroup.DefaultInstance.INSTANCE;
    }

    private MessageGroup() {

    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void createQueueByTag(Integer tag) {
        mMessageMap.putIfAbsent(tag, new MultithreadConcurrentQueue(2048));
    }

    public void pushMessage(TransferMsg buf) {
        try {
            int index = buf.getPlayerIndex();
            int tag = index % threadCount;
            mMessageMap.get(tag).offer(buf);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public TransferMsg popMessage(int tag) {
        return mMessageMap.get(tag).poll();
    }
}
