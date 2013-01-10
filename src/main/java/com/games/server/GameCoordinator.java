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
            case "createGame":
                return createGame(clientMessage);
            case "joinGame":
                return joinGame(clientMessage);
            case "leaveGame":
                return leaveGame(clientMessage);
            case "getPlayerList":
                return getPlayerList(clientMessage);
            case "getNextMessage":
                return getNextMessage(clientMessage);
            default:
                // all other messages are assumed to be game specific and are handled by the game itself
                GlobalGameCoordinator.getGame(clientMessage.getSender()).handleMessage(clientMessage);
                return null;
        }
    }

    @Callback
    private ServerMessage createGame(ClientMessage clientMessage) {
        GameOptions gameOptions = gameProvider.createGameOptions();
        // TODO: parse game options from client message
        Game game = gameProvider.createGame(this, gameOptions);
        // TODO: make game available to other players
        return new GameCreatedMessage(game);
    }

    @Callback
    private ServerMessage joinGame(ClientMessage clientMessage) {
        PlayerContainer player = clientMessage.getSender();
        if (player == null) {
            return new InvalidClientMessageMessage(clientMessage, "not logged in");
        }
        Game game;
        synchronized (this) {
            if (nextGame == null) {
                nextGame = gameProvider.createGame(this, gameProvider.createGameOptions());
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
        GameHelper.enqueueServerMessage(new GameFormedMessage(game), game.getPlayers());

        // hand off control to the game itself...
        game.startGame();
    }

    /**
     * Callback for if a player chooses to leave a game before its over.
     *
     * @param clientMessage the client message
     * @return the PlayerReplacedMessage server message
     */
    @Callback
    private ServerMessage leaveGame(ClientMessage clientMessage) {
        PlayerContainer player = clientMessage.getSender();
        assert player != null;
        Game game = GlobalGameCoordinator.getGame(player);
        if (game == null) {
            return new InvalidClientMessageMessage(clientMessage, "not in game");
        }
        String gameId = clientMessage.getStringAttribute("gameId");
        if (!gameId.equals(game.getId())) {
            return new InvalidClientMessageMessage(clientMessage, "invalid game id");
        }
        PlayerSummaryDetails oldSummaryDetails = player.getSummaryDetails();
        player.setPlayer(gameProvider.createAIPlayer(this, game));
        PlayerSummaryDetails newSummaryDetails = player.getSummaryDetails();
        return new PlayerReplacedMessage(game, oldSummaryDetails, newSummaryDetails);
    }

    @Callback
    private ServerMessage getPlayerList(ClientMessage clientMessage) {
        Game game = GlobalGameCoordinator.getGame(clientMessage.getStringAttribute("gameId"));
        return new PlayerListMessage(game);
    }

    @Callback
    private ServerMessage getNextMessage(ClientMessage clientMessage) throws InterruptedException {
        return null;
        //return GameHelper.getNextServerMessage(GlobalGameCoordinator.getPlayer(clientMessage.getSessionId()));
    }
}
