package com.games.euchre;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 *
 * An enumeration of the card ranks used in standard Euchre: nine through ace,
 * inclusive.
 */
public enum Rank {

    ACE("A"),
    KING("K"),
    QUEEN("Q"),
    JACK("J"),
    TEN("10"),
    NINE("9"),
    EIGHT("8"),
    SEVEN("7");

    /** The rank's short name. */
    private final String shortName;

    private Rank(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the short name of this rank.
     *
     * @return the short name
     */
    @Override
    public String toString() {
        return shortName;
    }
}
