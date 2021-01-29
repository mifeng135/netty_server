package core.redis;

public interface DelayedQueueListener<T> {
    void invoke(T t);
}
