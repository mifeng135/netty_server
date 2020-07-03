package com.game.server.server;

import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.redis.RedisManager;
import com.game.server.core.sql.MysqlBatchHandle;
import com.game.server.eventGroup.DBEventGroup;
import com.game.server.eventGroup.DBEventHandler;
import com.game.server.socket.ReceiveFromGate1;
import com.game.server.socket.SendToGate1;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBServer {

    public void start() {
        RedisManager.getInstance();
        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();
        MysqlBatchHandle.getInstance();

        new DBEventGroup(DBEventHandler.class, 2);
        SendToGate1.getInstance().start();
        new ReceiveFromGate1().start();

        System.out.println("server start success");
    }
}
