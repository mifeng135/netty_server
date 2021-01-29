package com.game.server;

import core.redis.DelayedQueueListener;
import core.redis.TaskDelayEvent;

public class TestDel implements DelayedQueueListener<TaskDelayEvent> {
    @Override
    public void invoke(TaskDelayEvent o) {
        System.out.println(o.getData());
    }
}
