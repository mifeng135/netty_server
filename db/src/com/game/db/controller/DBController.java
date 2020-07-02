package com.game.db.controller;


import com.game.db.bean.PlayerBean;
import com.game.db.constant.MsgCmdConstant;
import com.game.db.constant.SqlCmdConstant;
import com.game.db.core.annotation.Ctrl;
import com.game.db.core.annotation.CtrlCmd;
import com.game.db.core.annotation.SqlAnnotation;
import com.game.db.core.msg.MsgBean;
import com.game.db.core.redis.RedisManager;
import com.game.db.proto.DataBaseMsg;
import com.game.db.socket.SendToGate1;
import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/5/28.
 */
@Ctrl
public class DBController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_DB_PLAYER_INFO_R)
    public void playerInfo(int id, byte[] data) throws Exception {
        DataBaseMsg.playerInfoR playerInfoR = DataBaseMsg.playerInfoR.parseFrom(data);

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
