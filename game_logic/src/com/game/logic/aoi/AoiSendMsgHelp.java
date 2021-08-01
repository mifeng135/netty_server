package com.game.logic.aoi;

import com.game.logic.model.Player;
import com.game.logic.util.MsgUtil;
import core.msg.HeaderProto;
import core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.remote.common.Position;
import protocal.remote.scene.*;

import java.util.ArrayList;
import java.util.List;

import static constants.MsgConstant.*;

public class AoiSendMsgHelp {

    private static final Logger logger = LoggerFactory.getLogger(AoiSendMsgHelp.class);


    /**
     * 发送某个玩家离开场景
     *
     * @param noticeList 通知的列表
     * @param player     离开的玩家
     */
    public static void sendPlayerLeaveScene(Player player, List<Integer> noticeList) {
        if (noticeList.size() <= 0) {
            return;
        }
        HeaderProto headerProto = ProtoUtil.initHeaderProto(MSG_SCENE_PLAYER_LEAVE_PUSH);
        ScenePlayerLeavePush scenePlayerLeavePush = new ScenePlayerLeavePush();
        List<Integer> leaveList = new ArrayList<>();
        leaveList.add(player.getPlayerIndex());
        scenePlayerLeavePush.setLeaveList(leaveList);
        MsgUtil.sendMsgWithList(headerProto, scenePlayerLeavePush, noticeList);
    }


    /**
     * 发送玩家改变grid的时候 需要移除的player 和 需要绘制的 player
     *
     * @param enterLeftInfo
     * @param player
     */
    public static void sendPlayerChangeGrid(EnterLeftInfo enterLeftInfo, Player player) {
        List<Integer> playerLeaveList = enterLeftInfo.getLeavePlayerIndexList();
        List<Player> playerEnterList = enterLeftInfo.getPlayerEnterList();
        if (playerLeaveList.size() <= 0 && playerEnterList.size() <= 0) {
            return;
        }
        ScenePlayerChangeGridPush scenePlayerChangeGrid = new ScenePlayerChangeGridPush();
        List<PlayerEnterBean> playerEnterBeanList = new ArrayList<>();
        for (Player pl : playerEnterList) {
            PlayerEnterBean playerEnterBean = new PlayerEnterBean();
            playerEnterBean.setSceneId(pl.getSceneId());
            playerEnterBean.setPlayerIndex(pl.getPlayerIndex());
            playerEnterBean.setPositionX(pl.getPlayerSceneBean().getPlayerPositionX());
            playerEnterBean.setPositionY(pl.getPlayerSceneBean().getPlayerPositionY());
            playerEnterBean.setJob(pl.getPlayerRoleBean().getJob());
            playerEnterBean.setSex(pl.getPlayerRoleBean().getSex());
            playerEnterBeanList.add(playerEnterBean);
        }
        scenePlayerChangeGrid.setPlayerEnterBeanList(playerEnterBeanList);
        scenePlayerChangeGrid.setPlayerLeaveList(playerLeaveList);

        HeaderProto headerProto = ProtoUtil.initHeaderProto(MSG_SCENE_PLAYER_CHANGE_GRID, player.getPlayerIndex());
        MsgUtil.sendMsg(headerProto, scenePlayerChangeGrid);
    }

    /**
     * 发送玩家同步新
     *
     * @param x                 x坐标
     * @param y                 y坐标
     * @param noticeList        通知的玩家列表
     * @param ignorePlayerIndex 忽略发送的玩家
     */
    public static void sendSyncPosition(int x, int y, boolean firstMove, List<Integer> noticeList, int ignorePlayerIndex) {
        Position position = new Position(x, y);
        SyncPositionPush positionPush = new SyncPositionPush();
        positionPush.setPosition(position);
        positionPush.setPlayerIndex(ignorePlayerIndex);
        positionPush.setFirstMove(firstMove);
        HeaderProto headerProto = ProtoUtil.initHeaderProto(MSG_SCENE_SYNC_POSITION_PUSH);
        noticeList.remove(Integer.valueOf(ignorePlayerIndex));
        MsgUtil.sendMsgWithList(headerProto, positionPush, noticeList);
    }

    /**
     * 玩家进入场景 发送进入玩家周围的信息包含自己
     *
     * @param playerList
     */
    public static void sendPlayerEnterScene(List<Player> playerList, Player player) {

        List<Integer> noticeList = new ArrayList<>();
        List<PlayerEnterBean> noticeSelfList = new ArrayList<>();
        List<PlayerEnterBean> noticeOtherList = new ArrayList<>();

        for (Player pl : playerList) {
            PlayerEnterBean playerEnterBean = new PlayerEnterBean();
            playerEnterBean.setSceneId(pl.getSceneId());
            playerEnterBean.setPlayerIndex(pl.getPlayerIndex());
            playerEnterBean.setPositionX(pl.getPlayerSceneBean().getPlayerPositionX());
            playerEnterBean.setPositionY(pl.getPlayerSceneBean().getPlayerPositionY());
            playerEnterBean.setJob(pl.getPlayerRoleBean().getJob());
            playerEnterBean.setSex(pl.getPlayerRoleBean().getSex());
            noticeSelfList.add(playerEnterBean);
            if (pl.getPlayerIndex() != player.getPlayerIndex()) {
                noticeList.add(pl.getPlayerIndex());
            } else {
                noticeOtherList.add(playerEnterBean);
            }
        }

        ScenePlayerEnterPush allPlayer = new ScenePlayerEnterPush();
        allPlayer.setPlayerEnterBeanList(noticeSelfList);
        MsgUtil.sendMsg(ProtoUtil.initHeaderProto(MSG_SCENE_PLAYER_ENTER_PUSH, player.getPlayerIndex()), allPlayer); //发送自己

        ScenePlayerEnterPush enterPlayer = new ScenePlayerEnterPush();
        enterPlayer.setPlayerEnterBeanList(noticeOtherList);
        MsgUtil.sendMsgWithList(ProtoUtil.initHeaderProto(MSG_SCENE_PLAYER_ENTER_PUSH, player.getPlayerIndex()), enterPlayer, noticeList);
    }
}
