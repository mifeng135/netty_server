package com.game.server.core.util;

/**
 * Created by Administrator on 2020/7/6.
 */
public class TimeUtil {

    public static int getCurrentTimeSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
