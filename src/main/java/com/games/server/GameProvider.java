package com.games.server;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GameProvider {

    Game createGame(GameCoordinator gameCoordinator);

    AIPlayer createAIPlayer(GameCoordinator gameCoordinator);
}
