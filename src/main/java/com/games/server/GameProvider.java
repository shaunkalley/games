package com.games.server;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameProvider {

    public abstract GameOptions createGameOptions();

    public final Game createGame(GameCoordinator gameCoordinator) {
        return createGame(gameCoordinator, createGameOptions());
    }

    public abstract Game createGame(GameCoordinator gameCoordinator, GameOptions gameOptions);

    public abstract AIPlayer createAIPlayer(GameCoordinator gameCoordinator, Game game);
}
