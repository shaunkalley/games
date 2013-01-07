package com.games.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.games.server.messages.AckMessage;
import com.games.util.CalledBy;
import com.games.util.ReadOnlyCollectionResult;
import org.apache.commons.lang3.ArrayUtils;

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

    protected final List<Player> players;

    private final BlockingQueue<ClientMessage> clientMessageQueue;

    public Game(String gameId, GameCoordinator gameCoordinator) {
        this.gameId = gameId;
        this.gameCoordinator = gameCoordinator;
        players = new ArrayList<>();
        clientMessageQueue = new LinkedBlockingQueue<>();
    }

    public final String getId() {
        return gameId;
    }

    public final void addPlayer(Player player) {
        players.add(player);
    }

    public final void replacePlayer(Player player, Player replacementPlayer) {
        // TODO
    }

    /**
     * Returns a read-only list of the players currently in this game.
     *
     * @return a read-only list of the players currently in this game
     */
    @ReadOnlyCollectionResult
    public final List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Returns whether the game has enough players to start.
     *
     * @return whether the game has enough players to start
     */
    @CalledBy(GameCoordinator.class)
    public abstract boolean hasEnoughPlayers();

    /**
     * Transfers control to the game from the GameCoordinator once the game has
     * formed.
     */
    @CalledBy(GameCoordinator.class)
    public abstract void startGame();

    @CalledBy(GameCoordinator.class)
    public final AckMessage handleGameSpecificMessage(ClientMessage clientMessage) {
        clientMessageQueue.offer(clientMessage);
        return new AckMessage(clientMessage.getAction());
    }

    protected final ClientMessage getNextClientMessage(Player player, String... acceptableActions) throws InterruptedException {
        ClientMessage clientMessage;
        do {
            clientMessage = clientMessageQueue.take();
            Player messagePlayer = gameCoordinator.getPlayer(clientMessage.getSessionId());
            if (messagePlayer != player) {
                // TODO: send invalid player message
            } else if (ArrayUtils.contains(acceptableActions, clientMessage.getAction())) {
                // TODO: send invalid action message
            } else {
                break;
            }
        } while (!Thread.currentThread().isInterrupted());
        return clientMessage;
    }
}
