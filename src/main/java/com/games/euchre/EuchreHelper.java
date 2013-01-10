package com.games.euchre;

import com.games.server.PlayerContainer;
import static com.games.euchre.Rank.JACK;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-01
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 *
 * A set of Euchre helper methods.
 */
public final class EuchreHelper {

    private EuchreHelper() {
    }

    /**
     * Determines if a card is trump, which will be true if the card is of the
     * literal trump suit or if it is the left bower.
     *
     * @param card the card
     * @param trump the trump suit
     * @return if the card is trump
     */
    public static boolean isTrump(Card card, Suit trump) {
        return card.getSuit() == trump || isLeftBower(card, trump);
    }

    /**
     * Determines if a card is the right bower, which will be true if the card
     * is the jack of the literal trump suit.
     *
     * @param card the card
     * @param trump the trump suit
     * @return if the card is the right bower
     */
    public static boolean isRightBower(Card card, Suit trump) {
        return card.getRank() == JACK && card.getSuit() == trump;
    }

    /**
     * Determines if a card is the left bower which will be true if the card is
     * the jack of the next suit.
     *
     * @param card the card
     * @param trump the trump suit
     * @return if the card is the left bower
     */
    public static boolean isLeftBower(Card card, Suit trump) {
        return card.getRank() == JACK && trump.isNextSuit(card.getSuit());
    }

    /**
     * Determines the suit of a card, which will be the literal suit of the
     * card unless its a left bower in which case its suit is the trump suit.
     *
     * @param card the card
     * @param trump the trump suit
     * @return the suit of the card
     */
    public static Suit getSuit(Card card, Suit trump) {
        return isLeftBower(card, trump) ? trump : card.getSuit();
    }

    /**
     * Determines if two cards are of the same suit, which will be true if they
     * are both trump or if they are both not trump and are the same literal
     * suit.
     *
     * @param card1 the first card
     * @param card2 the second card
     * @param trump the trump suit
     * @return if the cards are of the same suit
     */
    public static boolean areSameSuit(Card card1, Card card2, Suit trump) {
        if (isTrump(card1, trump)) {
            return isTrump(card2, trump);
        } else {
            return !isTrump(card2, trump) && card1.getSuit() == card2.getSuit();
        }
    }

    /**
     * Determines the ordinality of a trump card, with 0 being the prime value
     * (reserved for the right bower).
     *
     * @param card the trump card
     * @param trump the trump suit (for verification)
     * @return the ordinality
     */
    public static int getTrumpOrdinality(Card card, Suit trump) {
        assert isTrump(card, trump);
        if (isRightBower(card, trump)) {
            return 0;
        } else if (isLeftBower(card, trump)) {
            return 1;
        } else  {
            switch (card.getRank()) {
                case ACE:
                    return 2;
                case KING:
                    return 3;
                case QUEEN:
                    return 4;
                case TEN:
                    return 5;
                case NINE:
                    return 6;
                case EIGHT:
                    return 7;
                case SEVEN:
                    return 8;
                default:
                    throw new AssertionError("unknown rank");
            }
        }
    }

    /**
     * Determines the ordinality of a non-trump card with 0 being the prime
     * value (reserved for aces).
     *
     * @param card the non-trump card
     * @param trump the trump suit (for verification)
     * @return the ordinality
     */
    public static int getNonTrumpOrdinality(Card card, Suit trump) {
        assert !isTrump(card, trump);
        switch (card.getRank()) {
            case ACE:
                return 0;
            case KING:
                return 1;
            case QUEEN:
                return 2;
            case JACK:
                return 3;
            case TEN:
                return 4;
            case NINE:
                return 5;
            case EIGHT:
                return 6;
            case SEVEN:
                return 7;
            default:
                throw new AssertionError("unknown rank");
        }
    }

    public static int getPoints(Trick trick) {
        Hand hand = trick.getHand();
        EuchreGameOptions gameOptions = trick.getHand().getGame().getGameOptions();
        PlayerContainer maker = trick.getHand().getMaker();
        Partnership makers = trick.getHand().getGame().getPartnership(maker);
        Partnership winners = trick.getWinners();
        int tricksCount = hand.getTricks().size();
        int makersTricksWon = hand.getTricksWon(makers);
        if (makers == winners) {
            if (hand.isMakerGoingAlone() && makersTricksWon == tricksCount) {
                return 4;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }
}
