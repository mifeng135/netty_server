package com.game.logic.controller;

import com.game.logic.manager.PlayerManager;
import com.game.logic.model.Player;
import com.game.logic.util.MathUtil;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import protocal.remote.common.Position;
import protocal.remote.scene.SyncPositionReq;

import static constants.MsgConstant.*;


@Ctrl
public class SyncPositionController {

    @CtrlCmd(cmd = MSG_POSITION_SYNC_PUSH)
    public void syncPlayerPosition(TransferMsg msg) {
        SyncPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SyncPositionReq.class);
        Player player = PlayerManager.getPlayer(msg.getHeaderProto().getPlayerIndex());

        Position position = MathUtil.calcDelayTimePosition(sycPositionReq.getPosition().getX(), sycPositionReq.getPosition().getY(),
                sycPositionReq.getDelayTime(), sycPositionReq.getMoveAngle());

        player.updatePlayerPosition(position.getX(), position.getY(), sycPositionReq.isFirstMove());
    }
}
