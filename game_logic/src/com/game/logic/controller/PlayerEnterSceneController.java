package com.game.logic.controller;


import com.game.logic.manager.PlayerManager;
import com.game.logic.manager.SceneManager;
import com.game.logic.model.Player;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;

import static constants.MsgConstant.MSG_ENTER_SCENE_FINISH;

@Ctrl
public class PlayerEnterSceneController {
    @CtrlCmd(cmd = MSG_ENTER_SCENE_FINISH)
    public void playerEnterScene(TransferMsg msg) {
        Player player = PlayerManager.getPlayer(msg.getHeaderProto().getPlayerIndex());
        SceneManager.playerEnterScene(player.getSceneId(), player);
    }
}
