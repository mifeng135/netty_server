package com.game.server.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static Map<Integer, Integer> sessionMap = new ConcurrentHashMap<>();

    public static void addSession(int playerIndex) {
        sessionMap.put(playerIndex, 1);
    }

    public static void removeSession(int playerIndex) {
        sessionMap.remove(playerIndex);
    }
}
