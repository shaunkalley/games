package com.games.euchre.messages;

import com.games.euchre.EuchreGame;
import com.games.euchre.Hand;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 5:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPassedBidMessage extends ServerMessage {

    public static final String ACTION = "playerPassedBid";

    public PlayerPassedBidMessage(EuchreGame game, Hand hand) {
        super(ACTION);
    }
}
