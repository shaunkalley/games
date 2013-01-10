package com.games.memmatch;

import com.games.server.AIPlayer;
import com.games.server.GameCoordinator;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-09
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MemMatchAIPlayer extends AIPlayer<MemMatchGame> {

    public MemMatchAIPlayer(GameCoordinator gameCoordinator, MemMatchGame game) {
        super(gameCoordinator, game);
    }
}
