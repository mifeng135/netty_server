package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import protocal.MsgConstant;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.EnterGameRsp;

import static config.ErrorConstants.ERROR_CODE_PLAYER_DO_NOT_HAS_PLAYER;
import static protocal.MsgConstant.DB_CMD_QUERY_ALL_PLAYER_INFO_REQ;
import static protocal.MsgConstant.MSG_ENTER_GAME_RSP;

@Ctrl
public class EnterGameController extends AsyncCompletionHandler<Integer> {

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ)
    public void playerEnterGame(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_QUERY_ALL_PLAYER_INFO_REQ);
        AsyncHttp.getInstance().postAsync(msg.getHeaderProto(), msg.getData(), this);
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        TransferMsg msg = ProtoUtil.decodeDBHttpMsg(response.getResponseBodyAsBytes());
        msg.getHeaderProto().setMsgId(MSG_ENTER_GAME_RSP);

        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getData(), PlayerAllInfoDB.class);
        EnterGameRsp enterGameRsp = new EnterGameRsp();
        if (playerAllInfoDB.getPlayerInfo() != null) {
            enterGameRsp.setPlayerAllInfoDB(playerAllInfoDB);
            MsgUtil.sendMsg(msg.getHeaderProto(), enterGameRsp);
        } else {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), ERROR_CODE_PLAYER_DO_NOT_HAS_PLAYER);
        }
        return 1;
    }
}
