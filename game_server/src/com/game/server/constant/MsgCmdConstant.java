package com.game.server.constant;

/**
 * Created by Administrator on 2020/6/18.
 */
public class MsgCmdConstant {


    public static final int MSG_CMD_SERVER_LINK_STATE_R = 2;

    public static final int MSG_CMD_GAME_BEGIN = 4000;

    public static final int MSG_CMD_GAME_CREATE_ROOM_R = 4001;
    public static final int MSG_CMD_GAME_CREATE_ROOM_S = 4002;

    public static final int MSG_CMD_GAME_JOIN_ROOM_R = 4003;
    public static final int MSG_CMD_GAME_JOIN_ROOM_S = 4004;

    public static final int MSG_CMD_GAME_PLAYER_LEFT_ROOM_S = 4005;


    public static final int MSG_CMD_GAME_ROOM_PLAYER_LIST_R = 4006;
    public static final int MSG_CMD_GAME_ROOM_PLAYER_LIST_S = 4007;

    public static final int MSG_CMD_GAME_READY_R = 4008;
    public static final int MSG_CMD_GAME_READY_S = 4009;
    public static final int MSG_CMD_GAME_START_S = 4010;



    public static final int MSG_CMD_PLAYER_BOMB_PLACE_R = 4011;
    public static final int MSG_CMD_PLAYER_BOMB_PLACE_S = 4012;


    public static final int MSG_CMD_PLAYER_POSITION_R = 4013;
    public static final int MSG_CMD_PLAYER_POSITION_S = 4014;

    public static final int MSG_CMD_PLAYER_SYN_POSITION_R = 4015;
    public static final int MSG_CMD_PLAYER_SYN_POSITION_S = 4016;

    public static final int MSG_CMD_GAME_END = 4999;
}
