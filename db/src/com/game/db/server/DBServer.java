package com.game.db.server;

import com.game.db.core.annotation.CtrlAnnotation;
import com.game.db.core.annotation.SqlAnnotation;
import com.game.db.core.redis.RedisManager;
import com.game.db.core.sql.MysqlBatchHandle;
import com.game.db.eventGroup.DBEventGroup;
import com.game.db.eventGroup.DBEventHandler;
import com.game.db.socket.ReceiveFromGate1;
import com.game.db.socket.SendToGate1;

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

        System.out.println("db start success");
    }
}
