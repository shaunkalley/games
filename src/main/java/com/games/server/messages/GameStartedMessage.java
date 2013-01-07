package com.games.server.messages;

import com.games.server.Game;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GameStartedMessage extends ServerMessage {

    public static final String ACTION = "gameStarted";

    public GameStartedMessage(Game game) {
        super(ACTION, GAME_ID_KEY, game.getId());
    }
}
