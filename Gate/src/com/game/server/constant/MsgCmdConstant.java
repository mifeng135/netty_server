package com.game.server.constant;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MsgCmdConstant {

    /**
     * 发送登录消息
     */
    public static final int MSG_CMD_LOGIN_TO_GATE_R = 1;
    /**
     * 同步到登录服当前gate的链接数
     */
    public static final int MSG_CMD_SERVER_LINK_STATE_S = 2;

    /**
     * 如果msgbean中的sub cmd 等于此值 gate 将要进行广播
     */
    public static final short MSG_BROAD_CASE = 3;

    /**
     *心跳消息
     */
    public static final int MSG_HEART_BEAT_R = 4;


    /**
     * 分组广播
     */
    public static final short MSG_BROAD_CASE_GROUP = 5;

    /**
     * 同步连接数 发送给登录服
     */
    public static final int MSG_CMD_LINK_SYN_S = 100;
    /**
     * 顶号
     */
    public static final int MSG_CMD_REPLACE_ACCOUNT_S = 101;


    /**
     * db 消息区间
     */
    public static final int MSG_CMD_DB_BEGIN = 3000;
    public static final int MSG_CMD_DB_END = 3999;

    /**
     * game 消息区间
     */
    public static final int MSG_CMD_GAME_BEGIN = 4000;
    public static final int MSG_CMD_GAME_END = 4999;
}
