package core.sql;


import com.conversantmedia.util.concurrent.MultithreadConcurrentQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SyncSql extends Thread {

    private static Logger logger = LoggerFactory.getLogger(SyncSql.class);

    private MultithreadConcurrentQueue<String> queue = new MultithreadConcurrentQueue(2048);
    private volatile boolean quit = false;

    private static class DefaultInstance {
        static final SyncSql INSTANCE = new SyncSql();
    }

    public static SyncSql getInstance() {
        return SyncSql.DefaultInstance.INSTANCE;
    }

    private SyncSql() {

    }

    public void add(String playerIndex) {
        queue.offer(playerIndex);
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                String playerIndex = queue.poll();
                if (playerIndex != null) {
                    process(playerIndex);
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void quit() {
        quit = true;
        while (!queue.isEmpty()) {
            String playerIndex = queue.poll();
            process(playerIndex);
        }
    }


    public void process(String playerIndex) {
        try {
            Thread.sleep(2000);
            logger.info("playerIndex = {}", playerIndex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
