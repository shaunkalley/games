package com.games.server;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientMessage extends Message {

    private final PlayerContainer sender;

    public ClientMessage(PlayerContainer sender, Map<String, Object> attributes) {
        super(attributes);
        this.sender = sender;
    }

    public PlayerContainer getSender() {
        return sender;
    }
}
