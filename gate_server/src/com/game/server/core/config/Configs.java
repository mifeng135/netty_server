package com.game.server.core.config;

import io.netty.util.AttributeKey;

/**
 * Created by Administrator on 2020/5/28.
 */
public class Configs {

    /**是否开启 DELAY 算法*/
    public static final boolean TCP_NO_DELAY_DEFAULT = true;

    /**是否开启 REUSEADDR */
    public static final boolean TCP_SO_REUSEADDR_DEFAULT = true;

    public static final boolean TCP_SO_KEEP_ALIVE_DEFAULT = true;

    /**是否开启 超时*/
    public static final boolean NETTY_OPEN_IDLE = true;

    public static final int NETTY_IO_RATIO_DEFAULT = 70;

    public static final int TCP_SO_BACKLOG_DEFAULT = 1024;

    public static final int NETTY_BUFFER_HIGH_WATERMARK_DEFAULT = 64 * 1024;

    public static final int NETTY_BUFFER_LOW_WATERMARK_DEFAULT = 32 * 1024;

    /**设置超时时间  30秒为 前端为发生请求 则主动关闭连接*/
    public static final int TCP_SERVER_IDLE_DEFAULT = 60;

    public static final AttributeKey<Integer> PLAYER_INDEX =  AttributeKey.valueOf("PLAYER_INDEX");

    public static final String SERVER_PACKAGE_NAME = "com.game.server";
}
