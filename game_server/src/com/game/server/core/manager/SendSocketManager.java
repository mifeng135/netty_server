package com.game.server.core.manager;

import com.game.server.core.zero.Send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/7/28.
 */
public class SendSocketManager {

    private Map<Integer, Send> sendMap = new HashMap<>();
    private List<Integer> sendServerKeyList = new ArrayList<>();

    private static class DefaultInstance {
        static final SendSocketManager INSTANCE = new SendSocketManager();
    }

    public static SendSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SendSocketManager() {

    }
    public Send getSocket(int serverKey) {
        return sendMap.get(serverKey);
    }

    public void putSocket(int serverKey, Send send) {
        sendMap.put(serverKey, send);
        sendServerKeyList.add(serverKey);
    }

    public Map<Integer,Send> getSendMap() {
        return sendMap;
    }

    public List<Integer> getSendServerKeyList() {
        return sendServerKeyList;
    }
}
