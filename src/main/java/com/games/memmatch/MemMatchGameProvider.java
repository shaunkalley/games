package com.games.memmatch;

import com.games.server.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-09
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MemMatchGameProvider extends GameProvider {

    @Override
    public MemMatchGameOptions createGameOptions() {
        return new MemMatchGameOptions();
    }

    @Override
    public MemMatchGame createGame(GameCoordinator gameCoordinator, GameOptions gameOptions) {
        return new MemMatchGame(gameCoordinator, (MemMatchGameOptions) gameOptions);
    }

    @Override
    public MemMatchAIPlayer createAIPlayer(GameCoordinator gameCoordinator, Game game) {
        return new RandomMemMatchAIPlayer(gameCoordinator, (MemMatchGame) game);
    }
}
