package com.games.euchre.messages;

import java.util.List;

import com.games.euchre.Card;
import com.games.euchre.EuchreGame;
import com.games.euchre.Hand;
import com.games.server.ServerMessage;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 4:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardsDealtMessage extends ServerMessage {

    public static final String ACTION = "cardsDealt";

    public CardsDealtMessage(EuchreGame game, Hand hand, List<Card> cards) {
        super(ACTION, GAME_ID_KEY, game.getId(), "handNumber", hand.getNumber(), "cards", cards);
    }
}
