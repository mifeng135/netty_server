package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.RedisConstant;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.redis.RedisManager;
import core.util.ProtoUtil;
import core.util.StringUtil;
import core.util.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.MsgConstant;
import protocol.local.db.login.DBLoginReq;
import protocol.local.db.login.DBLoginRsp;
import protocol.remote.login.LoginReq;
import protocol.remote.login.LoginRsp;
import protocol.remote.login.RegisterReq;
import protocol.remote.system.ErroRsp;
import org.asynchttpclient.*;

import static config.Config.DB_HTTP_URL;
import static core.Constants.MSG_RESULT_FAIL;
import static protocol.MsgConstant.MSG_DB_QUERY_LOGIN;
import static protocol.MsgConstant.MSG_LOGIN_RSP;

/**
 * Created by Administrator on 2020/6/23.
 */

@Ctrl
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_LOGIN_REQ)
    public void login(TransferMsg msgBean, ChannelHandlerContext context) {
        LoginReq loginReq = ProtoUtil.deserializer(msgBean.getData(), LoginReq.class);

        RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        PlayerBean playerBean = loginMap.get(loginReq.getAccount());
        if (playerBean == null || playerBean.getPassword().equals(loginReq.getPassword())) {
            DBLoginReq dbLoginReq = new DBLoginReq();
            dbLoginReq.setAccount(loginReq.getAccount());
            dbLoginReq.setPwd(loginReq.getPassword());
            dbLoginReq.setQueryMsgId(MsgConstant.MSG_LOGIN_REQ);
            dbLoginReq.setQueryPlayerIndex(msgBean.getPlayerIndex());
            dbLoginReq.setPlayerIndex(msgBean.getPlayerIndex());


            long start = System.currentTimeMillis();
            byte[] data = AsyncHttp.getInstance().postSync(DB_HTTP_URL, MSG_DB_QUERY_LOGIN, dbLoginReq);
            long end = System.currentTimeMillis();
            logger.info("time dis = {}", end - start);
            DBLoginRsp dbLoginRsp = ProtoUtil.deserializer(data, DBLoginRsp.class);
            if (dbLoginRsp.getResult() == MSG_RESULT_FAIL) {
                ErroRsp erroRsp = new ErroRsp();
                erroRsp.setErrorStr("");
                HttpUtil.sendErrorMsg(dbLoginRsp.getPlayerIndex(), MSG_LOGIN_RSP, erroRsp);
            } else {
                LoginRsp loginRsp = new LoginRsp();
                loginRsp.setIp("127.0.0.1:7005");
                loginRsp.setPlayerIndex(dbLoginRsp.getId());
                loginRsp.setName(dbLoginRsp.getName());
                HttpUtil.sendMsg(loginRsp.getPlayerIndex(), MSG_LOGIN_RSP, loginRsp);
            }
//            AsyncHttp.postAsync(DB_HTTP_URL, MSG_DB_QUERY_LOGIN, dbLoginReq, new AsyncCompletionHandler<Integer>() {
//                @Override
//                public Integer onCompleted(Response response) throws Exception {
//                    DBLoginRsp dbLoginRsp = ProtoUtil.deserializer(response.getResponseBodyAsBytes(), DBLoginRsp.class);
//                    if (dbLoginRsp.getResult() == MSG_RESULT_FAIL) {
//                        ErroRsp erroRsp = new ErroRsp();
//                        erroRsp.setErrorStr("");
//                        HttpUtil.sendErrorMsg(dbLoginRsp.getPlayerIndex(), MSG_LOGIN_RSP, erroRsp);
//                    } else {
//                        LoginRsp loginRsp = new LoginRsp();
//                        loginRsp.setIp("127.0.0.1:7005");
//                        loginRsp.setPlayerIndex(dbLoginRsp.getId());
//                        loginRsp.setName(dbLoginRsp.getName());
//                        HttpUtil.sendMsg(loginRsp.getPlayerIndex(), MSG_LOGIN_RSP, loginRsp);
//                    }
//                    return response.getStatusCode();
//                }
//            });
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


//
//        RMap<String, PlayerBean> userMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
//        if (userMap.get(account) != null) {
//            ErroRsp erroRsp = new ErroRsp();
//            erroRsp.setErrorStr("帐号已经存在");
//            erroRsp.setMsgId(MsgConstant.MSG_REGISTER_RSP);
//            HttpUtil.sendErrorMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, erroRsp);
//            return;
//        }
//
//        Channel channel = HttpConnectManager.getConnect(msgBean.getPlayerIndex());
//        String loginIp = channel.attr(Constants.REMOTE_ADDRESS).get();
//        int registerTime = TimeUtil.getCurrentTimeSecond();
//
//
//        PlayerBean playerBean = new PlayerBean();
//        playerBean.setLastLoginTime(registerTime);
//        playerBean.setAccount(account);
//        playerBean.setLoginIp(loginIp);
//        playerBean.setPassword(pwd);
//        playerBean.setRegisterTime(registerTime);
//
//        if (result != -1) {
//            userMap.fastPut(account, playerBean);
//            DBRegisterRsp registerRsp = new DBRegisterRsp();
//            HttpUtil.sendMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_REGISTER_RSP, registerRsp);
//        }
    }
}
