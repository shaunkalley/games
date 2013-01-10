package com.games.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 *
 * Manages the components common to all games.
 */
public final class GlobalGameCoordinator {

    private static final GlobalGameCoordinator INSTANCE = new GlobalGameCoordinator();

    /** Mapping of web session IDs to players. */
    private final Map<String, PlayerContainer> sessionIdPlayerMap;

    /** Mapping of game IDs to games. */
    private final Map<String, Game> gameMap;

    /** Mapping of players to games. **/
    private final Map<PlayerContainer, Game> playerGameMap;

    private GlobalGameCoordinator() {
        sessionIdPlayerMap = new HashMap<>();
        gameMap = new HashMap<>();
        playerGameMap = new HashMap<>();
    }

    public static void addPlayer(String sessionId, PlayerContainer player) {
        INSTANCE.sessionIdPlayerMap.put(sessionId, player);
    }

    public static PlayerContainer getPlayer(String sessionId) {
        return INSTANCE.sessionIdPlayerMap.get(sessionId);
    }

    public static void removePlayer(String sessionId) {
        INSTANCE.sessionIdPlayerMap.remove(sessionId);
    }

    public static void addGame(String gameId, Game game) {
        INSTANCE.gameMap.put(gameId, game);
    }

    public static Game getGame(String gameId) {
        return INSTANCE.gameMap.get(gameId);
    }

    public static void removeGame(String gameId) {
        INSTANCE.gameMap.remove(gameId);
    }

    public static void playerAddedToGame(PlayerContainer player, Game game) {
        INSTANCE.playerGameMap.put(player, game);
    }

    public static Game getGame(PlayerContainer player) {
        return INSTANCE.playerGameMap.get(player);
    }
}
