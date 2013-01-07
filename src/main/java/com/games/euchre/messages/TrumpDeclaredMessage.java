package com.games.euchre.messages;

import com.games.euchre.EuchreGame;
import com.games.euchre.Hand;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrumpDeclaredMessage extends ServerMessage {

    public static final String ACTION = "trumpDeclared";

    public TrumpDeclaredMessage(EuchreGame game, Hand hand) {
        super(ACTION, GAME_ID_KEY, game.getId());
    }
}
