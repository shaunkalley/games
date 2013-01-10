package com.games.euchre;

import com.games.server.Game;
import com.games.server.GameCoordinator;
import com.games.server.GameOptions;
import com.games.server.GameProvider;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class EuchreGameProvider extends GameProvider {

    private static String generateGameId() {
        return "Euchre-" + RandomStringUtils.randomAlphanumeric(32);
    }

    public EuchreGameProvider() {
    }

    public EuchreGameOptions createGameOptions() {
        return new EuchreGameOptions();
    }

    public EuchreGame createGame(GameCoordinator gameCoordinator, GameOptions gameOptions) {
        return new EuchreGame(generateGameId(), gameCoordinator, (EuchreGameOptions) gameOptions);
    }

    public EuchreAIPlayer createAIPlayer(GameCoordinator gameCoordinator, Game game) {
        return new RandomCardEuchreAIPlayer(gameCoordinator, (EuchreGame) game);
    }
}
