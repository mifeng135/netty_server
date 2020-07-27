package com.game.server.constant;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MsgCmdConstant {

    /**
     * 玩家掉线 或者 链接 状态发生变化
     */
    public static final int MSG_CMD_SERVER_LINK_STATE_R = 2;

    /**
     * 全部广播
     */
    public static final short MSG_BROAD_CASE = 3;

    /**
     * 分组广播
     */
    public static final short MSG_BROAD_CASE_GROUP = 5;

    /**
     * 游戏协议开始范围
     */
    public static final int MSG_CMD_GAME_BEGIN = 4000;

    public static final int MSG_CMD_GAME_CREATE_ROOM_R = 4001;
    public static final int MSG_CMD_GAME_CREATE_ROOM_S = 4002;

    public static final int MSG_CMD_GAME_JOIN_ROOM_R = 4003;
    public static final int MSG_CMD_GAME_JOIN_ROOM_S = 4004;

    public static final int MSG_CMD_GAME_PLAYER_LEFT_ROOM_S = 4005;

    public static final int MSG_CMD_GAME_ROOM_LIST_S = 4006;
    public static final int MSG_CMD_GAME_ROOM_LIST_R = 4007;

    public static final int MSG_CMD_GAME_READY_R = 4008;
    public static final int MSG_CMD_GAME_READY_S = 4009;
    public static final int MSG_CMD_GAME_START_S = 4010;


    public static final int MSG_CMD_PLAYER_BOMB_PLACE_R = 4011;
    public static final int MSG_CMD_PLAYER_BOMB_PLACE_S = 4012;


    public static final int MSG_CMD_BOMB_EXPLODE_R = 4013;
    public static final int MSG_CMD_BOMB_EXPLODE_S = 4014;


    public static final int MSG_CMD_PLAYER_SYN_POSITION_R = 4015;
    public static final int MSG_CMD_PLAYER_SYN_POSITION_S = 4016;


    public static final int MSG_CMD_GAME_OVER_R = 4017;
    public static final int MSG_CMD_GAME_OVER_S = 4018;

    public static final int MSG_CMD_GAME_CREATE_PROP_R = 4019;
    public static final int MSG_CMD_GAME_CREATE_PROP_S = 4020;

    public static final int MSG_CMD_GAME_TRIGGER_PROP_R = 4021;
    public static final int MSG_CMD_GAME_TRIGGER_PROP_S = 4022;


    public static final int MSG_CMD_GAME_TILE_POSITION_SYN_R = 4023;
    public static final int MSG_CMD_GAME_AIRPLANE_PROP_S = 4024;

    public static final int MSG_CMD_GAME_EXIT_ROOM_R = 4025;
    public static final int MSG_CMD_GAME_EXIT_ROOM_S = 4026;

    public static final int MSG_CMD_GAME_END = 4999;
}
