package com.games.server.messages;

import com.games.server.Message;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public final class InvalidClientMessageMessage extends ServerMessage {

    public static final String ACTION = "invalidMessage";

    public InvalidClientMessageMessage(Message clientMessage, String reason) {
        super(ACTION, "clientMessage", clientMessage, "reason", reason);
    }
}
