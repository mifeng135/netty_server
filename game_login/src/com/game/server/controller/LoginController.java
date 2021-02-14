package com.game.server.controller;

import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.bean.PlayerBean;
import com.game.server.util.HttpUtil;
import com.game.server.util.TcpUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.db.PlayerInfoLoginReq;
import protocol.local.db.PlayerInfoLoginRsp;
import protocol.remote.login.LoginReq;
import protocol.remote.login.LoginRsp;
import protocol.remote.login.RegisterReq;
import protocol.remote.login.RegisterRsp;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.manager.HttpConnectManager;
import core.util.ProtoUtil;
import core.msg.TransferMsg;
import core.redis.RedisManager;
import core.util.StringUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.MsgConstant;
import protocol.remote.system.ErroRsp;

import static core.Constants.MSG_RESULT_FAIL;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_LOGIN_REQ)
    public void login(TransferMsg msgBean, ChannelHandlerContext context) {
        LoginReq loginReq = ProtoUtil.deserializer(msgBean.getData(), LoginReq.class);
        PlayerInfoLoginReq playerBaseInfoReq = new PlayerInfoLoginReq();
        playerBaseInfoReq.setAccount(loginReq.getAccount());
        playerBaseInfoReq.setPwd(loginReq.getPassword());
        playerBaseInfoReq.setQueryMsgId(MsgConstant.MSG_LOGIN_REQ);
        playerBaseInfoReq.setQueryPlayerIndex(msgBean.getPlayerIndex());
        playerBaseInfoReq.setPlayerIndex(msgBean.getPlayerIndex());
        TcpUtil.sendToDB(MsgConstant.MSG_DB_PLAYER_INFO_LOGIN_REQ, playerBaseInfoReq);
    }

    @CtrlCmd(cmd = MsgConstant.MSG_DB_PLAYER_INFO_LOGIN_RSP)
    public void onDBLoginAccountPwd(TransferMsg msgBean, ChannelHandlerContext context) {
        PlayerInfoLoginRsp playerInfoLoginRsp = ProtoUtil.deserializer(msgBean.getData(), PlayerInfoLoginRsp.class);
        int reqPlayerIndex = playerInfoLoginRsp.getPlayerIndex();
        int result = playerInfoLoginRsp.getResult();
        if (result == MSG_RESULT_FAIL) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("");
            erroRsp.setMsgId(MsgConstant.MSG_LOGIN_RSP);
            HttpUtil.sendErrorMsg(playerInfoLoginRsp.getQueryPlayerIndex(), MsgConstant.MSG_LOGIN_RSP, erroRsp);
            return;
        }
        LoginRsp loginRsp = new LoginRsp();
        loginRsp.setIp("127.0.0.1:7005");
        loginRsp.setPlayerIndex(playerInfoLoginRsp.getId());
        loginRsp.setName(playerInfoLoginRsp.getName());
        HttpUtil.sendMsg(reqPlayerIndex, MsgConstant.MSG_LOGIN_RSP, loginRsp);
    }

    @CtrlCmd(cmd = MsgConstant.MSG_REGISTER_REQ)
    public void register(TransferMsg msgBean, ChannelHandlerContext context) {
        RegisterReq registerReq = ProtoUtil.deserializer(msgBean.getData(), RegisterReq.class);

        String account = registerReq.getAccount();
        String pwd = registerReq.getPwd();
        String confirmPwd = registerReq.getConfirmPwd();
        String nickName = registerReq.getNickName();

        if (StringUtil.isEmpty(account) || StringUtil.isEmpty(pwd) || StringUtil.isEmpty(confirmPwd)) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("帐号或密码为空");
            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
            HttpUtil.sendErrorMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, erroRsp);
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("密码不一致");
            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
            HttpUtil.sendErrorMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, erroRsp);
            return;
        }

        RMap<String, PlayerBean> userMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        if (userMap.get(account) != null) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("帐号已经存在");
            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
            HttpUtil.sendErrorMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, erroRsp);
            return;
        }

        Channel channel = HttpConnectManager.getConnect(msgBean.getPlayerIndex());
        String loginIp = channel.attr(Constants.REMOTE_ADDRESS).get();
        int registerTime = TimeUtil.getCurrentTimeSecond();


        PlayerBean playerBean = new PlayerBean();
        playerBean.setLastLoginTime(registerTime);
        playerBean.setAccount(account);
        playerBean.setLoginIp(loginIp);
        playerBean.setPassword(pwd);
        playerBean.setRegisterTime(registerTime);

        int result = SqlAnnotation.getInstance().executeCommitSql(SqlCmdConstant.PLAYER_INSERT_REGISTER, playerBean);

        if (result != -1) {
            userMap.fastPut(account, playerBean);
            RegisterRsp registerRsp = new RegisterRsp();
            HttpUtil.sendMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, registerRsp);
        }
    }
}

