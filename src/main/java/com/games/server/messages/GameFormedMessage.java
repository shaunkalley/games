package com.games.server.messages;

import com.games.server.Game;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GameFormedMessage extends ServerMessage {

    public static final String ACTION = "gameFormed";

    public GameFormedMessage(Game game) {
        super(ACTION, "gameId", game.getId());
    }
}
