package com.game.server.core.coreUtil;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/6/24.
 */
public class TimerHolder {

    private final static long defaultTickDuration = 10;

    private static class DefaultInstance {
        static final Timer INSTANCE = new HashedWheelTimer(defaultTickDuration, TimeUnit.MILLISECONDS);
    }

    private TimerHolder() {
    }

    /**
     * Get a singleton instance of {@link Timer}. <br>
     * The tick duration is {@link #defaultTickDuration}.
     * @return Timer
     */
    public static Timer getTimer() {
        return DefaultInstance.INSTANCE;
    }
}
