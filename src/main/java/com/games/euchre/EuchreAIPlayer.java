package com.games.euchre;

import com.games.server.AIPlayer;
import com.games.server.GameCoordinator;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EuchreAIPlayer extends AIPlayer {

    public EuchreAIPlayer(GameCoordinator gameCoordinator) {
        super(gameCoordinator);
    }
}
