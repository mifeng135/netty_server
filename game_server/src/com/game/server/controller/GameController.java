package com.game.server.controller;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.Constant;
import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.core.redis.RedisManager;
import com.game.server.proto.ProtoJoinRoomS;
import com.game.server.proto.ProtoPlayerLeftS;
import com.game.server.room.Player;
import com.game.server.room.PlayerManager;
import com.game.server.room.Room;
import com.game.server.room.RoomManager;
import com.game.server.socket.SendToGate;
import org.redisson.api.RMap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/7/3.
 */

@Ctrl
public class GameController {


}
