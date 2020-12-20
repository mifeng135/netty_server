package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.bean.PlayerBean;
import com.game.server.manager.LinkSynManager;
import com.game.server.proto.ProtoLoginR;
import com.game.server.proto.ProtoLoginS;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.Configs;
import core.manager.HttpConnectManager;
import core.proto.ProtoUtil;
import core.proto.TransferMsg;
import core.redis.RedisManager;
import core.sql.MysqlBatchHandle;
import core.sql.MysqlBean;
import core.util.SocketUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_LOGIN_R)
    public void login(TransferMsg msgBean) throws Exception {

        ProtoLoginR loginR = ProtoUtil.deserializer(msgBean.getData(), ProtoLoginR.class);
        String account = loginR.getAccount();
        String password = loginR.getPassword();

        RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        PlayerBean playerBean = loginMap.get(account);

        boolean loginSuc = false;
        if (playerBean == null) {
            playerBean = new PlayerBean();
            playerBean.setAccount(account);
            playerBean.setPassword(password);
            PlayerBean result = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
            if (result != null) {
                playerBean = result;
                loginSuc = true;
            }
        } else if (playerBean.getPassword().equals(password)) {
            loginSuc = true;
        }

        if (loginSuc) {
            Channel channel = HttpConnectManager.getConnect(msgBean.getPlayerIndex());
            String loginIp = channel.attr(Configs.REMOTE_ADDRESS).get();
            int loginTime = TimeUtil.getCurrentTimeSecond();

            playerBean.setLastLoginTime(loginTime);
            playerBean.setLoginIp(loginIp);
            loginMap.fastPut(playerBean.getAccount(), playerBean);
        }

        MysqlBean sqlBean = new MysqlBean();
        sqlBean.setCmd(SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO);
        sqlBean.setData(playerBean);
        MysqlBatchHandle.getInstance().pushMsg(sqlBean);

        ProtoLoginS loginS = new ProtoLoginS();
        loginS.setRet(-1);
        if (loginSuc) {
            loginS.setRet(0);
            String ip = LinkSynManager.getInstance().getMinLinkCountIp();
            loginS.setIp("127.0.0.1:7005");
            loginS.setPlayerIndex(playerBean.getId());
            loginS.setName(playerBean.getName());
        }
        SocketUtil.sendHttpMsg(msgBean.getPlayerIndex(), MsgCmdConstant.MSG_CMD_LOGIN_S, ProtoUtil.serialize(loginS));
    }
}


