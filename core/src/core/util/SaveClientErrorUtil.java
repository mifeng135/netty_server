package core.util;

import core.msg.ClientExceptionPush;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveClientErrorUtil {

    private ConcurrentLinkedDeque<ClientExceptionPush> queue = new ConcurrentLinkedDeque();

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

    public void pushErrorMsg(ClientExceptionPush exceptionMsg) {
        queue.offer(exceptionMsg);
    }

    public void process() {
        if (!queue.isEmpty()) {
            List<ClientExceptionPush> errorList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ClientExceptionPush exceptionMsg = queue.poll();
                if (exceptionMsg == null) {
                    break;
                }
                errorList.add(exceptionMsg);
            }
            FileUtil.writeClientError(errorList);
        }
    }
}
