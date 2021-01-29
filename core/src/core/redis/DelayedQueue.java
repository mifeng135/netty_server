package core.redis;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class DelayedQueue {

    private final RedissonClient redisClient;

    public DelayedQueue(RedissonClient client) {
        redisClient = client;
    }

    public <T> void addQueue(T t, long delay, TimeUnit timeUnit) {
        RBlockingQueue<T> blockingFairQueue = redisClient.getBlockingQueue(t.getClass().getName());
        RDelayedQueue<T> delayedQueue = redisClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }
    /**
     * 获取队列
     *
     * @param zClass            DTO泛型
     * @param taskEventListener 任务回调监听
     * @param <T>               泛型
     * @return
     */
    public <T> void start(Class zClass, DelayedQueueListener taskEventListener) {
        RBlockingQueue<T> blockingFairQueue = redisClient.getBlockingQueue(zClass.getName());
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    T t = blockingFairQueue.take();
                    taskEventListener.invoke(t);
                } catch (Exception e) {

                }
            }
        });
        thread.start();
    }
}
