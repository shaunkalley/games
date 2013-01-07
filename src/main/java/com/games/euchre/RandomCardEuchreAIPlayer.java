package com.games.euchre;

import com.games.server.GameCoordinator;
import com.games.server.Message;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 *
 * An artificial-intelligence Euchre player that plays following suit but plays
 * random cards.
 */
public class RandomCardEuchreAIPlayer extends EuchreAIPlayer {

    public RandomCardEuchreAIPlayer(GameCoordinator gameCoordinator) {
        super(gameCoordinator);
    }

    public void handleMessage(Message message) {
        // TODO
    }
}
