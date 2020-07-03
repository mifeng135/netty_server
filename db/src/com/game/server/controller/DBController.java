package com.game.server.controller;


import com.game.server.bean.PlayerBean;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.redis.RedisManager;
import com.game.server.proto.DataBaseMsg;
import com.game.server.socket.SendToGate1;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/5/28.
 */
@Ctrl
public class DBController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_DB_PLAYER_INFO_R)
    public void playerInfo(int id, byte[] data) throws Exception {

        RMap map = RedisManager.getInstance().getRedisSon().getMap("PLAYER");
        PlayerBean bean = (PlayerBean) map.get(id);
        if (bean == null) {
            bean = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_USER_INFO, id);
            map.fastPutIfAbsent(bean.getId(),bean);
        }

        DataBaseMsg.playerInfoS.Builder playerInfoS = DataBaseMsg.playerInfoS.newBuilder();
        playerInfoS.setId(id);
        playerInfoS.setName(bean.getName());

        MsgBean msgBean = new MsgBean();
        msgBean.setId(id);
        msgBean.setCmd(MsgCmdConstant.MSG_CMD_DB_PLAYER_INFO_S);
        msgBean.setData(playerInfoS.build().toByteArray());
        SendToGate1.getInstance().pushSendMsg(msgBean);
    }
}
