package com.games.server.messages;

import com.games.server.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerLeftGameMessage extends ServerMessage {

    public static final String ACTION = "playerLeftGame";

    public PlayerLeftGameMessage(Game game, HumanPlayer player, AIPlayer replacementPlayer) {
        super(ACTION, GAME_ID_KEY, game.getId(), "player", player, "replacementPlayer", replacementPlayer);
    }
}
