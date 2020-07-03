package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.Constant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.redis.RedisManager;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/6/18.
 */

@Ctrl
public class RoomController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_CREATE_ROOM_R)
    public void createRoom(int id, byte[] data) {

        RMap map = RedisManager.getInstance().getRedisSon().getMap(Constant.REDIS_PLAYER_KEY);
        PlayerBean bean = (PlayerBean) map.get(id);

    }
    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_GAME_JOIN_ROOM_R)
    public void joinRoom(int id, byte[] data) {
        RMap map = RedisManager.getInstance().getRedisSon().getMap("");
    }


}
