package com.game.server.core.manager;

import com.game.server.core.zero.Receive;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/7/29.
 */
public class ReceiveSocketManager {

    private Map<Integer, Receive> receiveMap = new HashMap<>();

    private static class DefaultInstance {
        static final ReceiveSocketManager INSTANCE = new ReceiveSocketManager();
    }

    public static ReceiveSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public Receive getSocket(int serverKey) {
        return receiveMap.get(serverKey);
    }

    public void putSocket(int serverKey, Receive send) {
        receiveMap.put(serverKey, send);
    }
}
