package com.game.game.controller;

import com.game.game.constant.MsgCmdConstant;
import com.game.game.core.annotation.Ctrl;
import com.game.game.core.annotation.CtrlCmd;

/**
 * Created by Administrator on 2020/6/18.
 */

@Ctrl
public class RoomController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R)
    public void createRoom(int id, byte[] data) {

    }
}
