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

    private Map<Byte, Send> sendMap = new HashMap<>();
    private List<Byte> sendServerKeyList = new ArrayList<>();

    private static class DefaultInstance {
        static final SendSocketManager INSTANCE = new SendSocketManager();
    }

    public static SendSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SendSocketManager() {

    }
    public Send getSocket(byte serverKey) {
        return sendMap.get(serverKey);
    }

    public void putSocket(byte serverKey, Send send) {
        sendMap.put(serverKey, send);
        sendServerKeyList.add(serverKey);
    }

    public Map<Byte,Send> getSendMap() {
        return sendMap;
    }

    public List<Byte> getSendServerKeyList() {
        return sendServerKeyList;
    }
}
