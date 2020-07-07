package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.config.Configs;
import com.game.server.core.msg.MsgUtil;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.redis.RedisManager;
import com.game.server.bean.PlayerBean;
import com.game.server.core.sql.MysqlBatchHandle;
import com.game.server.core.sql.MysqlBean;
import com.game.server.core.util.TimeUtil;
import com.game.server.manager.LinkSynManager;
import com.game.server.proto.ProtoLoginR;
import com.game.server.proto.ProtoLoginS;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_LOGIN_R)
    public void login(ChannelHandlerContext context, byte[] data) throws Exception {

        ProtoLoginR loginR = ProtoUtil.deserializer(data, ProtoLoginR.class);
        String account = loginR.getAccount();
        String password = loginR.getPassword();


        RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        RMap<Integer, PlayerBean> playerMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_PLAYER_KYE);


        PlayerBean playerBean = loginMap.get(account);

        int loginTime = TimeUtil.getCurrentTimeSecond();
        String loginIp = context.channel().attr(Configs.REMOTE_ADDRESS).get();

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
            playerBean.setLoginIp(loginIp);
            playerBean.setLastLoginTime(loginTime);
            loginMap.fastPut(playerBean.getAccount(), playerBean);
            playerMap.fastPut(playerBean.getId(), playerBean);
        }

        MysqlBean sqlBean = new MysqlBean();
        sqlBean.setCmd(SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO);
        sqlBean.setData(playerBean);
        MysqlBatchHandle.getInstance().pushMsg(sqlBean);

        ProtoLoginS loginS = new ProtoLoginS();

        if (loginSuc) {
            loginS.setRet(0);
            String ip = LinkSynManager.getInstance().getMinLinkCountIp();
            loginS.setIp(ip);
            loginS.setPlayerIndex(playerBean.getId());
            loginS.setName(playerBean.getName());
        } else {
            loginS.setRet(-1);
        }
        MsgUtil.sendMsg(context, MsgCmdConstant.MSG_CMD_LOGIN_S, ProtoUtil.serialize(loginS));
    }
}


