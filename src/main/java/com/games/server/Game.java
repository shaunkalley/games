package com.games.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.games.util.CalledBy;
import com.games.util.ReadOnlyCollectionResult;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Game {

    private final String gameId;

    protected final GameCoordinator gameCoordinator;

    protected final List<PlayerContainer> players;

    private GameAction currentAction;

    public Game(String gameId, GameCoordinator gameCoordinator) {
        this.gameId = gameId;
        this.gameCoordinator = gameCoordinator;
        players = new ArrayList<>();
    }

    public final String getId() {
        return gameId;
    }

    public final void addPlayer(PlayerContainer player) {
        players.add(player);
    }

    /**
     * Returns a read-only list of the players currently in this game.
     *
     * @return a read-only list of the players currently in this game
     */
    @ReadOnlyCollectionResult
    public final List<PlayerContainer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public abstract boolean hasEnoughPlayers();

    /**
     * Transfers control to the game from the GameCoordinator once the game has
     * formed.
     */
    @CalledBy(GameCoordinator.class)
    public final void startGame() {
        initGame();
        setNextAction();
    }

    protected abstract void initGame();

    @CalledBy(GameCoordinator.class)
    public final void handleMessage(ClientMessage clientMessage) throws InvalidActionException {
        assert currentAction != null;
        currentAction.clientMessageReceived(clientMessage);
    }

    public final void setNextAction() {
        currentAction = getNextAction();
    }

    protected abstract GameAction getNextAction();
}
