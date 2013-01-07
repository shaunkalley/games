package com.games.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidActionException extends RuntimeException {

    private final ClientMessage clientMessage;

    private final List<String> validActions;

    public InvalidActionException(ClientMessage clientMessage, List<String> validActions) {
        this.clientMessage = clientMessage;
        this.validActions = Collections.unmodifiableList(validActions);
    }

    public InvalidActionException(ClientMessage clientMessage, String... validActions) {
        this(clientMessage, Arrays.asList(validActions));
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public List<String> getValidActions() {
        return validActions;
    }
}
