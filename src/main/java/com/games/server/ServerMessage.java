package com.games.server;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerMessage extends Message {

    public ServerMessage(String action, Object... attributes) {
        super(ArrayUtils.addAll(attributes, ACTION_KEY, action));
    }

    public final String toJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getAttributes());
    }
}
