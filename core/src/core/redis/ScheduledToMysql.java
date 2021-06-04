package core.redis;


import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledToMysql {

    private ConcurrentLinkedDeque<RedisToSqlBean> queue = new ConcurrentLinkedDeque();

    public ScheduledToMysql() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(() -> {



        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void push(RedisToSqlBean bean) {
        queue.offer(bean);
    }
}
