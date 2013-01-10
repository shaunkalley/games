package com.games.server.messages;

import com.games.server.Game;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-08
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameCreatedMessage extends ServerMessage {

    public static final String ACTION = "gameCreated";

    public GameCreatedMessage(Game game) {
        super(ACTION, "gameId", game.getId());
    }
}
