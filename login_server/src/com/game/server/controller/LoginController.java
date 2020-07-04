package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.msg.MsgUtil;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.redis.RedisManager;
import com.game.server.bean.PlayerBean;
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

        boolean loginSuc = false;

        RMap map = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        PlayerBean playerBean = (PlayerBean) map.get(account);

        if (playerBean == null) {
            playerBean = new PlayerBean();
            playerBean.setAccount(account);
            playerBean.setPassword(password);
            PlayerBean result = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
            if (result != null) {
                RMap playerMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_PLAYER_KYE);
                playerBean = result;
                map.fastPutIfAbsent(result.getAccount(), result);
                playerMap.fastPutIfAbsent(result.getId(), result);
                loginSuc = true;
            }
        } else {
            loginSuc = true;
        }

        ProtoLoginS loginS = new ProtoLoginS();
        if (loginSuc) {
            loginS.setRet(-1);
            String ip = LinkSynManager.getInstance().getMinLinkCountIp();
            loginS.setIp(ip);
            loginS.setPlayerIndex(playerBean.getId());
        } else {
            loginS.setRet(-1);
        }
        MsgUtil.sendMsg(context, MsgCmdConstant.MSG_CMD_LOGIN_S, ProtoUtil.serialize(loginS));
    }
}


