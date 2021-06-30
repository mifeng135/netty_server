package com.game.logic.controller;


import com.game.logic.aoi.AoiSendMsgHelp;
import com.game.logic.manager.PlayerManager;
import com.game.logic.manager.SceneManager;
import com.game.logic.model.Player;
import com.game.logic.model.Scene;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;

import java.util.List;

import static core.msg.SysMsgConstants.MSG_REMOTE_SOCKET_CLOSE_PUSH;

@Ctrl
public class SocketCloseController {

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg) {
        Player player = PlayerManager.getPlayer(msg.getHeaderProto().getPlayerIndex());
        if (player == null) {
            return;
        }
        int gridId = player.getCurrentGridId();
        int sceneId = player.getSceneId();
        Scene scene = SceneManager.getScene(sceneId);
        List<Integer> playerList = scene.getAoiManager().getPlayerIndexList(gridId);
        AoiSendMsgHelp.sendPlayerLeaveScene(player, playerList);
        SceneManager.getScene(sceneId).getAoiManager().getGrid(gridId).removePlayer(player);
        PlayerManager.removePlayer(msg.getHeaderProto().getPlayerIndex());
    }

    private void processDb(Player player) {

    }
}
