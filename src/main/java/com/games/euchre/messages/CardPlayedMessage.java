package com.games.euchre.messages;

import com.games.euchre.Card;
import com.games.euchre.Trick;
import com.games.server.Player;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-07
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardPlayedMessage extends ServerMessage {

    public static final String ACTION = "cardPlayed";

    public CardPlayedMessage(Trick trick, Player player, Card card) {
        super(ACTION,
            GAME_ID_KEY, trick.getHand().getGame().getId(),
            "handNumber", trick.getHand().getNumber(),
            "trickNumber", trick.getNumber(),
            "player", player,
            "card", card.toString());
    }
}
