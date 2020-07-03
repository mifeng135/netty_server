package com.game.server.constant;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MsgCmdConstant {

    /**
     * 发送登录消息
     */
    public static final int MSG_CMD_LOGIN_TO_GATE_R = 1;
    public static final int MSG_CMD_SERVER_LINK_STATE = 2;

    /**
     * 同步连接数 发送
     */

    public static final int MSG_CMD_LINK_SYN_S = 100;




    public static final int MSG_CMD_DB_BEGIN = 3000;
    public static final int MSG_CMD_DB_PLAYER_INFO_R = 3001;
    public static final int MSG_CMD_DB_PLAYER_INFO_S = 3002;
    public static final int MSG_CMD_DB_END = 3999;


    public static final int MSG_CMD_GAME_BEGIN = 4000;
    public static final int MSG_CMD_GAME_JOIN_ROOM_R = 4001;
    public static final int MSG_CMD_GAME_JOIN_ROOM_S = 4002;
    public static final int MSG_CMD_GAME_CREATE_ROOM_R = 4003;
    public static final int MSG_CMD_GAME_CREATE_ROOM_S = 4004;
    public static final int MSG_CMD_GAME_END = 4999;
}
