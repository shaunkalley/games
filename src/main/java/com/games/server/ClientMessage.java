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

    private final String sessionId;

    public ClientMessage(String sessionId, Map<String, Object> attributes) {
        super(attributes);
        if (sessionId == null) {
            throw new IllegalArgumentException("session id cannot be null");
        }
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
