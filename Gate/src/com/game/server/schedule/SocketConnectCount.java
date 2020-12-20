package com.game.server.schedule;


import core.manager.WebSocketConnectionManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/6/24.
 */
public class SocketConnectCount {

    /**30秒同步一次链接次数*/
    public SocketConnectCount() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                int size = WebSocketConnectionManager.getConnectSize();

            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
