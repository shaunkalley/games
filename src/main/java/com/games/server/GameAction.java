package com.games.server;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-09
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 *
 * Handles change in game state according to the following protocol:
 * <ol>
 *     <li>Wait for ACKs from all players.</li>
 *     <li>Send message to all players with game state and next player.</li>
 *     <li>Wait for reply from next player.</li>
 *     <li>Update game state.</li>
 *     <li>Send message to all players with game state.</li>
 * </ol>
 */
public abstract class GameAction<T extends Game> {

    protected final T game;

    private final List<PlayerContainer> players;

    public GameAction(T game) {
        this.game = game;
        players = new LinkedList<>(game.getPlayers());
    }

    public final void clientMessageReceived(ClientMessage clientMessage) throws InvalidActionException {

        if (!players.isEmpty() && !clientMessage.getAction().equals(Message.ACK)) {
            throw new InvalidActionException(clientMessage, players, Message.ACK);
        }

        if (clientMessage.getAction().equals(Message.ACK) && !players.remove(clientMessage.getSender())) {
            throw new InvalidActionException(clientMessage, players, Message.ACK);
        }

        if (clientMessage.getAction().equals(Message.ACK) && players.isEmpty()) {
            sendGameStateAndNextPlayerMessage();
        } else {
            updateGameState(clientMessage);
            sendGameStateMessage();
            game.setNextAction();
        }
    }

    protected abstract void sendGameStateAndNextPlayerMessage();

    protected abstract void updateGameState(ClientMessage clientMessage) throws InvalidActionException;

    protected abstract void sendGameStateMessage();
}
