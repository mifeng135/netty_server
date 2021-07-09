package com.game.logic.controller;


import bean.db.player.PlayerSceneBean;
import com.game.logic.aoi.AoiSendMsgHelp;
import com.game.logic.manager.PlayerManager;
import com.game.logic.manager.SceneManager;
import com.game.logic.model.Player;
import com.game.logic.model.Scene;
import constants.MsgConstant;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.local.db.PlayerOffline;

import java.util.List;

import static core.msg.SysMsgConstants.MSG_REMOTE_SOCKET_CLOSE_PUSH;

@Ctrl
public class SocketCloseController {

    private static final Logger logger = LoggerFactory.getLogger(SocketCloseController.class);

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg) {
        Player player = PlayerManager.getPlayer(msg.getHeaderProto().getPlayerIndex());
        if (player == null) {
            return;
        }
        logger.info("player offline playerIndex = {}" , player.getPlayerIndex());

        int gridId = player.getCurrentGridId();
        int sceneId = player.getSceneId();
        Scene scene = SceneManager.getScene(sceneId);
        List<Integer> playerList = scene.getAoiManager().getPlayerIndexList(gridId);
        AoiSendMsgHelp.sendPlayerLeaveScene(player, playerList);
        processDb(player);
        SceneManager.getScene(sceneId).getAoiManager().getGrid(gridId).removePlayer(player);
        PlayerManager.removePlayer(msg.getHeaderProto().getPlayerIndex());
    }

    private void processDb(Player player) {
        PlayerSceneBean playerSceneBean = player.getPlayerSceneBean();
        PlayerOffline playerOffline = new PlayerOffline();
        playerOffline.setPlayerSceneBean(playerSceneBean);
        AsyncHttp.getInstance().postAsync(ProtoUtil.initHeaderProto(MsgConstant.DB_CMD_PLAYER_OFFLINE), playerOffline);
    }
}
