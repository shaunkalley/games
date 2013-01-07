package com.games.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.games.server.messages.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 *
 * Responsible for managing logged-in users and assigning them to games.
 */
public final class GameCoordinator {

    private static final Logger logger = Logger.getLogger(GameCoordinator.class.getName());

    private final GameProvider gameProvider;

    /** Two-way mapping of session ids to players. */
    private final BiMap<String, Player> sessionPlayerMap;

    /** The game to form next. */
    private Game nextGame;

    private final Lock nextGameLock;

    /** Mapping of players to games. **/
    private final Map<Player, Game> playerGameMap;

    /** Mapping of players to pending-message queues. */
    private final Map<Player, BlockingQueue<ServerMessage>> playerMessageQueueMap;

    public GameCoordinator(GameProvider gameProvider) {
        this.gameProvider = gameProvider;
        sessionPlayerMap = HashBiMap.create(2048);
        nextGameLock = new ReentrantLock();
        playerGameMap = new HashMap<>();
        playerMessageQueueMap = new HashMap<>();
    }

    public void addPlayer(String sessionId, Player player) {
        sessionPlayerMap.put(sessionId, player);
        playerMessageQueueMap.put(player, new LinkedBlockingQueue<ServerMessage>());
    }

    public void removePlayer(String sessionId) {
        Player player = sessionPlayerMap.remove(sessionId);
        playerMessageQueueMap.remove(player);
    }

    public Player getPlayer(String sessionId) {
        return sessionPlayerMap.get(sessionId);
    }

    public String getSessionId(Player player) {
        return sessionPlayerMap.inverse().get(player);
    }

    public Game getGame(String sessionId) {
        return getGame(sessionPlayerMap.get(sessionId));
    }

    public Game getGame(Player player) {
        return playerGameMap.get(player);
    }

    private BlockingQueue<ServerMessage> getMessageQueue(Player player) {
        return playerMessageQueueMap.get(player);
    }

    public void addMessage(ServerMessage message, Player... players) {
        for (Player player : players) {
            getMessageQueue(player).offer(message);
        }
    }

    public void addMessage(ServerMessage message, Collection<Player> players) {
        for (Player player : players) {
            getMessageQueue(player).offer(message);
        }
    }

    public ServerMessage handleMessage(ClientMessage clientMessage) throws Exception {
        assert clientMessage != null;
        logger.info("Handling client message: action=" + clientMessage.getAction() + ", attributes=" + clientMessage.getAttributes());
        switch (clientMessage.getAction()) {
            case "joinGame":
                return joinGame(clientMessage);
            case "leaveGame":
                return leaveGame(clientMessage);
            case "getNextMessage":
                return getNextMessage(clientMessage);
            default:
                // all other messages are assumed to be game specific and are
                // handled by the game itself
                return getGame(clientMessage.getSessionId()).handleGameSpecificMessage(clientMessage);
        }
    }

    @Callback
    private ServerMessage joinGame(ClientMessage clientMessage) {
        Player player = getPlayer(clientMessage.getSessionId());
        if (player == null) {
            player = GlobalGameCoordinator.INSTANCE.getPlayer(clientMessage.getSessionId());
            if (player != null) {
                addPlayer(clientMessage.getSessionId(), player);
            }
        }
        if (player == null) {
            return new InvalidClientMessageMessage(clientMessage, "not logged in");
        }
        Game game;
        nextGameLock.lock();
        try {
            if (nextGame == null) {
                nextGame = gameProvider.createGame(this);
            }
            assert !nextGame.hasEnoughPlayers();
            nextGame.addPlayer(player);
            playerGameMap.put(player, nextGame);
            game = nextGame;
            if (nextGame.hasEnoughPlayers()) {
                gameFormed(nextGame);
                nextGame = null;
            }
        } finally {
            nextGameLock.unlock();
        }
        return new JoinedGameMessage(game);
    }

    /**
     * TODO
     *
     * @param game the game
     */
    private void gameFormed(final Game game) {
        // prepare a message for each player in the game notifying them that the game has formed
        for (Player player : nextGame.getPlayers()) {
            getMessageQueue(player).offer(new GameFormedMessage(game));
        }
        // prepare a message for each player in the game notifying them that the game has started
        for (Player player : nextGame.getPlayers()) {
            getMessageQueue(player).offer(new GameStartedMessage(game));
        }

        // hand off control to the game itself...
        new Thread() {
            public void run() {
                game.startGame();
            }
        }.start();
    }

    @Callback
    private ServerMessage leaveGame(ClientMessage clientMessage) {
        Player player = getPlayer(clientMessage.getSessionId());
        if (player == null) {
            return new InvalidClientMessageMessage(clientMessage, "not logged in");
        }
        Game game = getGame(player);
        if (game == null) {
            return new InvalidClientMessageMessage(clientMessage, "not in game");
        }
        String gameId = clientMessage.getStringAttribute("gameId");
        if (!gameId.equals(game.getId())) {
            return new InvalidClientMessageMessage(clientMessage, "invalid game id");
        }
        AIPlayer replacementPlayer = gameProvider.createAIPlayer(this);
        game.replacePlayer(player, replacementPlayer);
        return new PlayerLeftGameMessage(game, (HumanPlayer) player, replacementPlayer);
    }

    @Callback
    private ServerMessage getNextMessage(ClientMessage clientMessage) throws InterruptedException {
        Player player = getPlayer(clientMessage.getSessionId());
        BlockingQueue<ServerMessage> messageQueue = getMessageQueue(player);
        return messageQueue.take();
    }
}
