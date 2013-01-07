package com.games.server.messages;

import com.games.server.Game;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public final class JoinedGameMessage extends ServerMessage {

    public static final String ACTION = "joinedGame";

    public JoinedGameMessage(Game game) {
        super(ACTION, GAME_ID_KEY, game.getId(), "players", game.getPlayers());
    }
}
