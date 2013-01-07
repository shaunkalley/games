package com.games.euchre;

import com.games.server.*;
import com.games.server.Game;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class EuchreGameProvider implements GameProvider {

    private static String generateGameId() {
        return "Euchre-" + RandomStringUtils.randomAlphanumeric(24);
    }

    public EuchreGameProvider() {
    }

    public Game createGame(GameCoordinator gameCoordinator) {
        return new EuchreGame(generateGameId(), gameCoordinator);
    }

    public AIPlayer createAIPlayer(GameCoordinator gameCoordinator) {
        return new RandomCardEuchreAIPlayer(gameCoordinator);
    }
}
