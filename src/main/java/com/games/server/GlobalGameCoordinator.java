package com.games.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.RandomStringUtils;

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

    private static String generateLoginId() {
        return RandomStringUtils.randomAlphanumeric(24);
    }

    /** Two-way mapping of session ids to players. */
    private final BiMap<String, Player> sessionIdPlayerMap;

    private final BiMap<Player, String> playerLoginIdMap;

    private final Map<String, Game> gameMap;

    /** Mapping of players to games. **/
    private final Map<Player, Game> playerGameMap;

    /** Mapping of players to pending-message queues. */
    private final Map<Player, BlockingQueue<ServerMessage>> playerMessageQueueMap;

    private GlobalGameCoordinator() {
        sessionIdPlayerMap = HashBiMap.create(2048);
        playerLoginIdMap = HashBiMap.create(2048);
        gameMap = new HashMap<>();
        playerGameMap = new HashMap<>();
        playerMessageQueueMap = new HashMap<>();
    }

    public static String playerLoggedIn(String sessionId, Player player) {
        INSTANCE.sessionIdPlayerMap.put(sessionId, player);
        String loginId = generateLoginId();
        INSTANCE.playerLoginIdMap.put(player, loginId);
        INSTANCE.playerMessageQueueMap.put(player, new LinkedBlockingQueue<ServerMessage>());
        return loginId;
    }

    public static Player getPlayer(String sessionId) {
        return INSTANCE.sessionIdPlayerMap.get(sessionId);
    }

    public static String getLoginId(Player player) {
        return INSTANCE.playerLoginIdMap.get(player);
    }

    public static void gameCreated(Game game) {
        INSTANCE.gameMap.put(game.getId(), game);
    }

    public static Game getGame(String gameId) {
        return INSTANCE.gameMap.get(gameId);
    }

    public static void playerAddedToGame(Player player, Game game) {
        INSTANCE.playerGameMap.put(player, game);
    }

    public static Game getGame(Player player) {
        return INSTANCE.playerGameMap.get(player);
    }

    private static BlockingQueue<ServerMessage> getMessageQueue(Player player) {
        return INSTANCE.playerMessageQueueMap.get(player);
    }

    public static void enqueueMessage(Player player, ServerMessage message) {
        getMessageQueue(player).offer(message);
    }

    public static void enqueueMessage(ServerMessage message, Player... players) {
        for (Player player : players) {
            enqueueMessage(player, message);
        }
    }

    public static void enqueueMessage(ServerMessage message, Collection<Player> players) {
        for (Player player : players) {
            enqueueMessage(player, message);
        }
    }

    /**
     * Gets the next message that has been put on the player's queue.  This
     * operation will block until there is a message to take.
     *
     * @param player the player
     * @return the next message for the player
     * @throws InterruptedException if interrupted while waiting
     */
    public static ServerMessage getNextMessage(Player player) throws InterruptedException{
        return getMessageQueue(player).take();
    }
}
