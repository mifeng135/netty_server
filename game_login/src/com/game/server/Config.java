package com.game.server;

public class Config {
    public static final String HTTP_IP = "127.0.0.1";
    public static final int HTTP_PORT = 8000;

    public static final int LOGIC_THREAD_COUNT = 4;
    public static final String LOGIC_THREAD_NAME = "login";

    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PWD = "nqwl0520";
    public static final int REDIS_THREAD_COUNT = 2;
    public static final int REDIS_NETTY_THREAD_COUNT = 2;
}
