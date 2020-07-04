package com.game.server.controller;


import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;

/**
 * Created by Administrator on 2020/5/28.
 */
@Ctrl
public class DBController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_DB_PLAYER_INFO_R)
    public void playerInfo(int id, byte[] data) throws Exception {

    }
}
