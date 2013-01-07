package com.games.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalGameCoordinator {

    public static final GlobalGameCoordinator INSTANCE = new GlobalGameCoordinator();

    /** Two-way mapping of session ids to players. */
    private final BiMap<String, Player> sessionPlayerMap;

    /** Mapping of players to games. **/
    private final Map<Player, Game> playerGameMap;

    /** Mapping of players to pending-message queues. */
    private final Map<Player, BlockingQueue<Message>> playerMessageQueueMap;

    private GlobalGameCoordinator() {
        sessionPlayerMap = HashBiMap.create(2048);
        playerGameMap = new HashMap<>();
        playerMessageQueueMap = new HashMap<>();
    }

    public void addPlayer(String sessionId, Player player) {
        sessionPlayerMap.put(sessionId, player);
    }

    public Player getPlayer(String sessionId) {
        return sessionPlayerMap.get(sessionId);
    }
}
