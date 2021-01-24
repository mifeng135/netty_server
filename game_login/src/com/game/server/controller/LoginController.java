package com.game.server.controller;

import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.bean.PlayerBean;
import io.netty.channel.ChannelHandlerContext;
import protocol.login.LoginReq;
import protocol.login.LoginRsp;
import protocol.login.RegisterReq;
import protocol.login.RegisterRsp;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.manager.HttpConnectManager;
import core.util.ProtoUtil;
import core.msg.TransferMsg;
import core.redis.RedisManager;
import core.sql.MysqlBatchHandle;
import core.sql.MysqlBean;
import core.util.SocketUtil;
import core.util.StringUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.MsgConstant;
import protocol.system.ErroRsp;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_LOGIN_REQ)
    public void login(TransferMsg msgBean, ChannelHandlerContext context) {

        LoginReq loginReq = ProtoUtil.deserializer(msgBean.getData(), LoginReq.class);
        String account = loginReq.getAccount();
        String password = loginReq.getPassword();


        RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        PlayerBean playerBean = loginMap.get(account);

        boolean loginSuc = false;
        if (playerBean == null) {
            playerBean = new PlayerBean();
            playerBean.setAccount(account);
            playerBean.setPassword(password);
            PlayerBean result = SqlAnnotation.getInstance().sqlSelectOne(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
            if (result != null) {
                playerBean = result;
                loginSuc = true;
            }
        } else if (playerBean.getPassword().equals(password)) {
            loginSuc = true;
        }

        if (loginSuc) {
            Channel channel = HttpConnectManager.getConnect(msgBean.getSocketIndex());
            String loginIp = channel.attr(Constants.REMOTE_ADDRESS).get();
            int loginTime = TimeUtil.getCurrentTimeSecond();

            playerBean.setLastLoginTime(loginTime);
            playerBean.setLoginIp(loginIp);
            loginMap.fastPut(playerBean.getAccount(), playerBean);
        }

        MysqlBean sqlBean = new MysqlBean();
        sqlBean.setCmd(SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO);
        sqlBean.setData(playerBean);
        MysqlBatchHandle.getInstance().pushMsg(sqlBean);

        if (loginSuc) {
            LoginRsp loginRsp = new LoginRsp();
            loginRsp.setIp("127.0.0.1:7005");
            loginRsp.setPlayerIndex(playerBean.getId());
            loginRsp.setName(playerBean.getName());
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_LOGIN_RSP, MSG_RESULT_SUCCESS, loginRsp);
        } else {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("");
            erroRsp.setMsgId(MsgConstant.MSG_LOGIN_RSP);
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_LOGIN_RSP, MSG_RESULT_FAIL, erroRsp);
        }
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
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_REGISTER_RSP, MSG_RESULT_FAIL, erroRsp);
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("密码不一致");
            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_REGISTER_RSP, MSG_RESULT_FAIL, erroRsp);
            return;
        }

        RMap<String, PlayerBean> userMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        if (userMap.get(account) != null) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("帐号已经存在");
            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_REGISTER_RSP, MSG_RESULT_FAIL, erroRsp);
            return;
        }

        Channel channel = HttpConnectManager.getConnect(msgBean.getSocketIndex());
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
            SocketUtil.sendHttpMsg(msgBean.getSocketIndex(), MsgConstant.MSG_REGISTER_RSP, MSG_RESULT_SUCCESS, registerRsp);
        }
    }
}

