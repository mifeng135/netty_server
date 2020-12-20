package core.manager;

import core.zero.MPairSocket;

import java.util.HashMap;
import java.util.Map;

public class SocketManager {
    private Map<String, MPairSocket> receiveMap = new HashMap<>();

    private static class DefaultInstance {
        static final SocketManager INSTANCE = new SocketManager();
    }

    public static SocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public MPairSocket getSocket(String serverKey) {
        return receiveMap.get(serverKey);
    }

    public void putSocket(String serverKey, MPairSocket socket) {
        receiveMap.put(serverKey, socket);
    }
}
