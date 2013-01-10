package com.games.server.messages;

import com.games.server.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerReplacedMessage extends ServerMessage {

    public static final String ACTION = "playerReplaced";

    public PlayerReplacedMessage(Game game, PlayerSummaryDetails oldSummaryDetails, PlayerSummaryDetails newSummaryDetails) {
        super(ACTION, GAME_ID_KEY, game.getId(), "oldSummaryDetails", oldSummaryDetails, "newSummaryDetails", newSummaryDetails);
    }
}
