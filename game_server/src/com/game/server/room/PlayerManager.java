package com.game.server.room;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/7/3.
 */
public class PlayerManager {

    private static class DefaultInstance {
        static final PlayerManager INSTANCE = new PlayerManager();
    }

    public static PlayerManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private ConcurrentHashMap<Integer,Player> playerMap = new ConcurrentHashMap<>();

    public void putPlayer(Player player) {
        playerMap.putIfAbsent(player.getId(),player);
    }

    public Player removePlayer(int id) {
        return playerMap.remove(id);
    }


    public Player getPlayer(int id) {
        return playerMap.get(id);
    }

}
