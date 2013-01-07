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
public class PlayerJoinedGameMessage extends ServerMessage {

    public static final String ACTION = "playerJoinedGame";

    public PlayerJoinedGameMessage(String sessionId, Game game) {
        super(sessionId, ACTION, "gameId", game.getId(), "players", game.getPlayers());
    }
}
