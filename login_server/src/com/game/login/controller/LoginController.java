package com.game.login.controller;

import com.game.login.constant.MsgCmdConstant;
import com.game.login.constant.RedisConstant;
import com.game.login.constant.SqlCmdConstant;
import com.game.login.core.annotation.CtrlCmd;
import com.game.login.core.annotation.Ctrl;
import com.game.login.core.annotation.SqlAnnotation;
import com.game.login.core.msg.MsgUtil;
import com.game.login.core.redis.RedisManager;
import com.game.login.bean.PlayerBean;
import com.game.login.manager.LinkSynManager;
import com.game.login.proto.LoginMsg;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_LOGIN_R)
    public void login(ChannelHandlerContext context, byte[] data) throws Exception {
        LoginMsg.loginR loginR = LoginMsg.loginR.parseFrom(data);

        String account = loginR.getAccount();
        String password = loginR.getPassword();

        boolean loginSuc = false;

        RMap map = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_PLAYER_KEY);
        PlayerBean playerBean = (PlayerBean) map.get(account);

        if (playerBean == null) {
            playerBean = new PlayerBean();
            playerBean.setAccount(account);
            playerBean.setPassword(password);

            PlayerBean result = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
            if (result != null) {
                playerBean = result;
                map.fastPutIfAbsent(result.getAccount(), result);
                loginSuc = true;
            }
        } else {
            loginSuc = true;
        }

        LoginMsg.loginS.Builder loginS = LoginMsg.loginS.newBuilder();
        if (loginSuc) {
            loginS.setRet(MsgCmdConstant.MSG_RET_CODE_SUCCESS);
            String ip = LinkSynManager.getInstance().getMinLinkCountIp();
            loginS.setIp(ip);
            loginS.setPlayerIndex(playerBean.getId());
        } else {
            loginS.setRet(-1);
        }
        MsgUtil.sendMsg(context, MsgCmdConstant.MSG_CMD_LOGIN_S, loginS.build().toByteArray());
    }
}


