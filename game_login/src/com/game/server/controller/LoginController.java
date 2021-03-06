package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.RedisConstant;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.MsgConstant;
import protocol.local.base.HeaderProto;
import protocol.local.db.login.DBLoginReq;
import protocol.local.db.login.DBLoginRsp;
import protocol.remote.login.LoginReq;
import protocol.remote.login.LoginRsp;
import protocol.remote.system.ErroRsp;

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
        msgBean.getHeaderProto().setMsgId(MSG_DB_QUERY_LOGIN);
        TransferMsg transferMsg = AsyncHttp.getInstance().postSync(DB_HTTP_URL, msgBean.getHeaderProto(), msgBean.getData());
        DBLoginRsp dbLoginRsp = ProtoUtil.deserializer(transferMsg.getData(), DBLoginRsp.class);
        if (dbLoginRsp.getName() == null) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorStr("");
            HttpUtil.sendErrorMsg(transferMsg.getHeaderProto().getPlayerIndex(), MSG_LOGIN_RSP, erroRsp);
            return;
        }
        LoginRsp loginRsp = new LoginRsp();
        loginRsp.setIp("127.0.0.1:7000");
        loginRsp.setPlayerIndex(dbLoginRsp.getId());
        loginRsp.setName(dbLoginRsp.getName());
        HttpUtil.sendMsg(transferMsg.getHeaderProto().getPlayerIndex(), MSG_LOGIN_RSP, loginRsp);
    }
}
