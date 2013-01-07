package com.games.server.messages;

import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AckMessage extends ServerMessage {

    public static final String ACTION = "ack";

    public AckMessage(String clientAction) {
        super(ACTION, "clientAction", clientAction);
    }
}
