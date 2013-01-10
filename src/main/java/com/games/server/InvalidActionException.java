package com.games.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.games.util.ReadOnlyCollectionResult;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidActionException extends Exception {

    private final ClientMessage clientMessage;

    private final List<PlayerContainer> validPlayers;

    private final List<String> validActions;

    public InvalidActionException(ClientMessage clientMessage, PlayerContainer validPlayer, String... validActions) {
        this(clientMessage, Arrays.asList(validPlayer), validActions);
    }

    public InvalidActionException(ClientMessage clientMessage, List<PlayerContainer> validPlayers, String... validActions) {
        this.clientMessage = clientMessage;
        this.validPlayers = Collections.unmodifiableList(validPlayers);
        this.validActions = Collections.unmodifiableList(Arrays.asList(validActions));
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    @ReadOnlyCollectionResult
    public List<PlayerContainer> getValidPlayers() {
        return validPlayers;
    }

    @ReadOnlyCollectionResult
    public List<String> getValidActions() {
        return validActions;
    }
}
