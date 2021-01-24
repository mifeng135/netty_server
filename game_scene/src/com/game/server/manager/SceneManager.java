package com.game.server.manager;

import com.game.server.model.Scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneManager {
    private static Map<Integer, Scene> sceneMap = new ConcurrentHashMap<>();
}
