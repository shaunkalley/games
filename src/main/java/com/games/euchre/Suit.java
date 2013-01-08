package com.games.euchre;

import static com.games.euchre.Suit.Color.BLACK;
import static com.games.euchre.Suit.Color.RED;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 *
 * An enumeration of the card suits used in standard Euchre: (spades, hearts,
 * diamonds, and clubs), plus four additional suits to allow for 8-player
 * games.
 */
public enum Suit {

    SPADES(BLACK),
    HEARTS(RED),
    DIAMONDS(RED),
    CLUBS(BLACK),
    // TODO: decide on the remaining suits (see http://en.wikipedia.org/wiki/Suit_(cards), http://8suits.com/, http://www.fatpackcards.co.uk/)
    RED3(RED),
    BLACK3(BLACK),
    BLACK4(BLACK),
    RED4(RED);

    /** This suit's colour (black or red). */
    private final Color color;

    private Suit(Color color) {
        this.color = color;
    }

    /**
     * Gets the colour of this suit (black or red).
     *
     * @return this suit's colour
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the next suit of this suit, i.e., the other suit of the same
     * colour.
     *
     * @return the next suit
     */
    public Suit getNextSuit() {
        for (Suit suit : values()) {
            if (isNextSuit(suit)) {
                return suit;
            }
        }
        throw new AssertionError("next suit not found; should never happen");
    }

    /**
     * Determines if a suit is the next suit of this suit, i.e., whether it is
     * the other suit of the same colour.
     *
     * @param suit the suit
     * @return if the suit is the next suit
     */
    public boolean isNextSuit(Suit suit) {
        return suit.getColor() == color && suit != this;
    }

    /**
     * Returns the name of this suit.
     *
     * @return this suit's name
     */
    @Override
    public String toString() {
        return name();
    }

    /**
     * Simple enumeration of suit colours: black and red.
     */
    public enum Color {
        BLACK, RED
    }
}
