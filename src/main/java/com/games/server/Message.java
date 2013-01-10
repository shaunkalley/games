package com.games.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-03
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Message {

    public static final String ACTION_KEY = "action";

    public static final String ACK = "ack";

    public static final String GAME_ID_KEY = "gameId";

    private final Map<String, Object> attributes;

    public Message(Object... attributes) {
        assert attributes.length % 2 == 0;
        boolean actionFound = false;
        for (int i = 0; i < attributes.length; i += 2) {
            if (attributes[i].equals(ACTION_KEY)) {
                actionFound = true;
                break;
            }
        }
        if (!actionFound) {
            throw new IllegalArgumentException("action not found in attributes");
        }
        Map<String, Object> temp = new HashMap<>();
        for (int i = 0; i < attributes.length; i++) {
            String key = (String) attributes[i];
            temp.put(key, attributes[++i]);
        }
        this.attributes = Collections.unmodifiableMap(temp);
    }

    public Message(Map<String, Object> attributes) {
        if (!attributes.containsKey(ACTION_KEY)) {
            throw new IllegalArgumentException("action not found in attributes");
        }
        this.attributes = Collections.unmodifiableMap(attributes);
    }

    public String getAction() {
        return getStringAttribute(ACTION_KEY);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String getStringAttribute(String key) {
        return (String) getAttribute(key);
    }

    public boolean getBooleanAttribute(String key) {
        return Boolean.parseBoolean(getStringAttribute(key));
    }

    /**
     *  Returns a read-only map of the attributes of this message.
     *
     *  @return a read-only map of the attributes of this message
     */
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
