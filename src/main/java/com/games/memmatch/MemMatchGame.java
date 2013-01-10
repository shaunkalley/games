package com.games.memmatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.games.server.Game;
import com.games.server.GameAction;
import com.games.server.GameCoordinator;
import com.games.server.PlayerContainer;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-09
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemMatchGame extends Game {

    private static String generateGameId() {
        return "MemMatch-" + RandomStringUtils.randomAlphanumeric(32);
    }

    private final MemMatchGameOptions gameOptions;

    private List<PlayerContainer> playOrder;

    public MemMatchGame(GameCoordinator gameCoordinator, MemMatchGameOptions gameOptions) {
        super(generateGameId(), gameCoordinator);
        this.gameOptions = gameOptions;
    }

    @Override
    public boolean hasEnoughPlayers() {
        return players.size() == 2;
    }

    @Override
    protected void initGame() {

        // determine play order
        List<PlayerContainer> playOrder = new ArrayList<>(players);
        Collections.shuffle(playOrder);
        this.playOrder = Collections.unmodifiableList(playOrder);

        // create and shuffle a new deck of cards
    }

    @Override
    protected GameAction getNextAction() {
        // TODO
        return null;
    }
}
