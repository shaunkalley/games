package com.games.euchre;

import java.util.Map;

import com.games.server.JsonParsable;
import com.games.server.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 *
 * A standard playing card.
 */
public final class Card implements JsonParsable {

    private static final JsonParser<Card> jsonParser = new JsonParser<Card>() {

        @Override
        public String toJson(Card card) {
            return "{ \"Card\": { \"suit\": " + Suit.getJsonParser().toJson(card.getSuit()) + ", \"rank\": " + Rank.getJsonParser().toJson(card.getRank()) + "} }";
        }

        @Override
        public Card parseJson(String json) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> map = mapper.readValue(json, Map.class);
            Map<String, String> innerMap = map.get("Card");
            if (innerMap == null) {
                throw new AssertionError("");
            }
            String suitJson = innerMap.get("suit");
            if (suitJson == null) {
                throw new AssertionError("");
            }
            String rankJson = innerMap.get("rank");
            if (rankJson == null) {
                throw new AssertionError("");
            }
            return new Card(Suit.getJsonParser().parseJson(suitJson), Rank.getJsonParser().parseJson(rankJson));
        }
    };

    public static JsonParser<Card> getJsonParser() {
        return jsonParser;
    }

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
     * Returns a string representation of this card from its suit and rank.
     *
     * @return this card's string representation
     */
    @Override
    public String toString() {
        return rank.toString() + "/" + suit.toString();
    }
}
