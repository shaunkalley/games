package com.games.server.messages;

import com.games.server.Game;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-07
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerListMessage extends ServerMessage {

    public static final String ACTION = "playerList";

    public PlayerListMessage(Game game) {
        super(ACTION, "gameId", game.getId(), "players", game.getPlayers());
    }
}
