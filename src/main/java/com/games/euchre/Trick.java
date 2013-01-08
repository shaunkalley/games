package com.games.euchre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.games.server.Player;
import com.games.util.ReadOnlyCollectionResult;
import static com.games.euchre.EuchreHelper.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 *
 * A trick played as part of a hand of Euchre.
 */
public final class Trick {

    /** The hand this trick is played in. */
    private final Hand hand;

    private final int number;

    /** The players in order that they play in the trick. */
    private List<Player> playOrder;

    /** The cards in order that they were played in the trick. */
    private List<Card> cards;

    /** Internal counter for tracking turns. */
    private int turn;

    /** The winner of this trick. */
    private Player winner;

    public Trick(Hand hand, int number, List<Player> playOrder) {
        EuchreGameOptions gameOptions = hand.getGame().getGameOptions();
        int numberOfPlayers = gameOptions.getNumberOfPlayers();
        if (playOrder.size() != numberOfPlayers && playOrder.size() != numberOfPlayers - 1) {
            throw new IllegalArgumentException("");
        }
        this.hand = hand;
        this.number = number;
        this.playOrder = Collections.unmodifiableList(playOrder);
        cards = new ArrayList<>(playOrder.size());
        turn = 0;
    }

    public Hand getHand() {
        return hand;
    }

    public int getNumber() {
        return number;
    }

    @ReadOnlyCollectionResult
    public List<Player> getPlayOrder() {
        return playOrder;
    }

    public void cardPlayed(Player player, Card card) throws DidNotFollowSuitException {
        if (!isPlayersTurn(player)) {
            throw new AssertionError("not player's turn");
        }
        if (!hand.hasCard(player, card)) {
            throw new AssertionError("not player's card");
        }
        if (!isLeadTurn()) {
            if (turn > 0 && !didFollowSuit(player, card)) {
                throw new DidNotFollowSuitException(this, card);
            }
        }
        cards.add(card);
        hand.cardPlayed(player, card);
        turn++;
    }

    public void determineWinner() {
        if (turn != playOrder.size()) {
            throw new AssertionError("");
        }
        Suit trump = hand.getTrump();
        Suit leadSuit = cards.get(0).getSuit();
        Card winningCard = cards.get(0);
        Player winner = playOrder.get(0);
        for (int i = 1; i < playOrder.size(); i++) {
            Card card = cards.get(i);
            if (isHigherThan(card, winningCard, trump, leadSuit)) {
                winningCard = card;
                winner = playOrder.get(i);
            }
        }
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    public Partnership getWinners() {
        // TODO
        return null;
    }

    private boolean isLeadTurn() {
        return turn == 0;
    }

    private boolean isPlayersTurn(Player player) {
        return playOrder.get(turn) == player;
    }

    private boolean didFollowSuit(Player player, Card card) {
        Card leadCard = cards.get(0);
        assert leadCard != null;
        return card.getSuit() == leadCard.getSuit() || !hand.hasSuit(player, card.getSuit());
    }

    /**
     * Determines whether the left-hand card is higher than the right-hand
     * card in a trick.  The right-hand card is assumed to be the currently-
     * winning card, i.e., either trump or of the lead suit.
     *
     * @param lhs the left-hand card
     * @param rhs the right-hand card
     * @param trump the trump suit
     * @param leadSuit the suit that was lead
     * @return whether the left-hand card is higher than the right-hand card
     */
    private boolean isHigherThan(Card lhs, Card rhs, Suit trump, Suit leadSuit) {
        assert !lhs.equals(rhs);
        assert isTrump(rhs, trump) || rhs.getSuit() == leadSuit;
        boolean isLeftTrump = isTrump(lhs, trump);
        boolean isRightTrump = isTrump(rhs, trump);
        if (isLeftTrump) {
            if (!isRightTrump) {
                return true;
            } else {
                int leftOrdinality = getTrumpOrdinality(lhs, trump);
                int rightOrdinality = getTrumpOrdinality(rhs, trump);
                return leftOrdinality < rightOrdinality;
            }
        } else {
            if (isRightTrump || lhs.getSuit() != leadSuit) {
                return false;
            } else {
                int leftOrdinality = getNonTrumpOrdinality(lhs, trump);
                int rightOrdinality = getNonTrumpOrdinality(rhs, trump);
                return leftOrdinality < rightOrdinality;
            }
        }
    }
}
