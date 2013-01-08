package com.games.server;

import java.util.logging.Logger;

import com.games.server.messages.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 *
 * Responsible for coordinating game formation.
 */
public final class GameCoordinator {

    private static final Logger logger = Logger.getLogger(GameCoordinator.class.getName());

    private final GameProvider gameProvider;

    /** The game to form next. */
    private Game nextGame;

    public GameCoordinator(GameProvider gameProvider) {
        this.gameProvider = gameProvider;
    }

    public ServerMessage handleMessage(ClientMessage clientMessage) throws Exception {
        assert clientMessage != null;
        logger.info("Handling client message: action=" + clientMessage.getAction() + ", attributes=" + clientMessage.getAttributes());
        switch (clientMessage.getAction()) {
            case "joinGame":
                return joinGame(clientMessage);
            case "leaveGame":
                return leaveGame(clientMessage);
            case "getPlayerList":
                return getPlayerList(clientMessage);
            case "getNextMessage":
                return getNextMessage(clientMessage);
            default:
                // all other messages are assumed to be game specific and are
                // handled by the game itself
                return GlobalGameCoordinator.getGame(GlobalGameCoordinator.getPlayer(clientMessage.getSessionId())).handleGameSpecificMessage(clientMessage);
        }
    }

    @Callback
    private ServerMessage joinGame(ClientMessage clientMessage) {
        Player player = GlobalGameCoordinator.getPlayer(clientMessage.getSessionId());
        if (player == null) {
            return new InvalidClientMessageMessage(clientMessage, "not logged in");
        }
        Game game;
        synchronized (this) {
            if (nextGame == null) {
                nextGame = gameProvider.createGame(this);
            }
            assert !nextGame.hasEnoughPlayers();
            nextGame.addPlayer(player);
            GlobalGameCoordinator.playerAddedToGame(player, nextGame);
            game = nextGame;
            if (nextGame.hasEnoughPlayers()) {
                gameFormed(nextGame);
                nextGame = null;
            }
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
        GlobalGameCoordinator.enqueueMessage(new GameFormedMessage(game), game.getPlayers());

        // hand off control to the game itself...
        Thread gameThread = new Thread() {
            public void run() {
                game.startGame();
                // prepare a message for each player in the game notifying them that the game has started
                GlobalGameCoordinator.enqueueMessage(new GameStartedMessage(game), game.getPlayers());
            }
        };
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Callback
    private ServerMessage leaveGame(ClientMessage clientMessage) {
        Player player = GlobalGameCoordinator.getPlayer(clientMessage.getSessionId());
        if (player == null) {
            return new InvalidClientMessageMessage(clientMessage, "not logged in");
        }
        Game game = GlobalGameCoordinator.getGame(player);
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
    private ServerMessage getPlayerList(ClientMessage clientMessage) {
        Game game = GlobalGameCoordinator.getGame(clientMessage.getStringAttribute("gameId"));
        return new PlayerListMessage(game);
    }

    @Callback
    private ServerMessage getNextMessage(ClientMessage clientMessage) throws InterruptedException {
        return GlobalGameCoordinator.getNextMessage(GlobalGameCoordinator.getPlayer(clientMessage.getSessionId()));
    }
}
