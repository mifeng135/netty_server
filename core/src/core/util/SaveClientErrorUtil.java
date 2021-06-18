package core.util;

import core.group.MessageGroup;
import core.msg.ClientExceptionMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveClientErrorUtil {

    private ConcurrentLinkedDeque<ClientExceptionMsg> queue = new ConcurrentLinkedDeque();

    private static class DefaultInstance {
        static final SaveClientErrorUtil INSTANCE = new SaveClientErrorUtil();
    }

    public static SaveClientErrorUtil getInstance() {
        return SaveClientErrorUtil.DefaultInstance.INSTANCE;
    }

    public SaveClientErrorUtil() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                process();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public void pushErrorMsg(ClientExceptionMsg exceptionMsg) {
        queue.offer(exceptionMsg);
    }

    public void process() {
        if (!queue.isEmpty()) {
            List<ClientExceptionMsg> errorList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ClientExceptionMsg exceptionMsg = queue.poll();
                if (exceptionMsg == null) {
                    break;
                }
                errorList.add(exceptionMsg);
            }
            FileUtil.writeClientError(errorList);
        }
    }
}
