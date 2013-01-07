package com.games.euchre.messages;

import com.games.euchre.EuchreGame;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DealOrderSetMessage extends ServerMessage {

    public static final String ACTION = "dealOrderSet";

    public DealOrderSetMessage(EuchreGame game) {
        super(ACTION, GAME_ID_KEY, game.getId(), "dealOrder", game.getDealOrder(), "partnerships", game.getPartnerships());
    }
}
