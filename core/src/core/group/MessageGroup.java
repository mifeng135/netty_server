package core.group;


import com.conversantmedia.util.concurrent.MultithreadConcurrentQueue;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MessageGroup {

    private static final int queueCapacity = 2048;
    private static Logger logger = LoggerFactory.getLogger(MessageGroup.class);

    private Map<Integer, MultithreadConcurrentQueue<TransferMsg>> mMessageMap = new HashMap<>();
    private int threadCount;

    private static class DefaultInstance {
        static final MessageGroup INSTANCE = new MessageGroup();
    }

    public static MessageGroup getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private MessageGroup() {

    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void createQueueByTag(Integer tag) {
        mMessageMap.putIfAbsent(tag, new MultithreadConcurrentQueue(queueCapacity));
    }

    public void pushMessage(TransferMsg buf) {
        try {
            int index = buf.getHeaderProto().getPlayerIndex();
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
