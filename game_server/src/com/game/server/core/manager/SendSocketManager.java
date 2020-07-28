package com.game.server.core.manager;

import com.game.server.core.zero.Send;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/7/28.
 */
public class SendSocketManager {

    private Map<Integer, Send> sendMap = new HashMap<>();

    private static class DefaultInstance {
        static final SendSocketManager INSTANCE = new SendSocketManager();
    }

    public static SendSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public Send getSocket(int serverKey) {
        return sendMap.get(serverKey);
    }

    public void putSocket(int serverKey, Send send) {
        sendMap.put(serverKey, send);
    }
}
