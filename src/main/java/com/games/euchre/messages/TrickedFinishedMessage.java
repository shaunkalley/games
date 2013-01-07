package com.games.euchre.messages;

import com.games.euchre.EuchreGame;
import com.games.euchre.Trick;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrickedFinishedMessage extends ServerMessage {

    public static final String ACTION = "trickedFinished";

    public TrickedFinishedMessage(EuchreGame game, Trick trick) {
        super(ACTION, GAME_ID_KEY, game.getId());
    }
}
