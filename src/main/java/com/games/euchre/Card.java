package com.games.euchre;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.games.util.Immutable;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 *
 * A standard playing card.  The string representation for this class is
 * "{Suit}/{Rank}" where {Suit} is the enum name of the suit, and {Rank} is the
 * enum name of the rank, e.g., the ace of spades would be represented as
 * "SPADES/ACE".
 */
@Immutable
public final class Card {

    private static final String formatPattern = "{0}/{1}";
    private static final Pattern parsePattern = Pattern.compile("([A-Z]+)/(A-Z]+)");

    /** The card's suit. */
    private final Suit suit;

    /** The card's rank. */
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        if (suit == null) {
            throw new IllegalArgumentException("suit cannot be null");
        }
        if (rank == null) {
            throw new IllegalArgumentException("rank cannot be null");
        }
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the suit of this card.
     *
     * @return this card's suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the rank of this card.
     *
     * @return this card's rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Determines if another object is logically the same as this card, which
     * will be true if the other object is also a card and has the same suit
     * and rank as this one.
     *
     * @param o the other object
     * @return if the other object is logically the same as this card
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        Card rhs = (Card) o;
        return suit == rhs.suit && rank == rhs.rank;
    }

    /**
     * Returns a hash value for this card from its suit and rank.
     *
     * @return this card's hash value
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + suit.hashCode();
        result = result * 31 + rank.hashCode();
        return result;
    }

    /**
     * Returns a "{Suit}/{Rank}" string representation of this card.
     *
     * @return this card's string representation
     */
    @Override
    public String toString() {
        return MessageFormat.format(formatPattern, suit.name(), rank.name());
    }

    /**
     * Parse a "{Suit}/{Rank}" string representation of a card.
     *
     * @param input the string representation to parse
     * @return the resultant Card
     */
    public static Card parse(String input) {
        Matcher matcher = parsePattern.matcher(input);
        if (matcher.matches()) {
            Suit suit = Suit.valueOf(matcher.group(1));
            Rank rank = Rank.valueOf(matcher.group(2));
            return new Card(suit, rank);
        } else {
            throw new IllegalArgumentException("input doesn't match \"{Suit}/{Rank}\" pattern");
        }
    }
}
