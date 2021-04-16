package com.game.logic.manager;

import com.game.logic.model.Scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneManager {
    private static Map<Integer, Scene> sceneMap = new ConcurrentHashMap<>();
}
