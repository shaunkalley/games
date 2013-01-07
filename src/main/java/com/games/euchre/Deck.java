package com.games.euchre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.games.util.ReadOnlyCollectionResult;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 *
 * A deck of cards.
 */
public final class Deck implements Iterable<Card> {

    private List<Card> cards;

    /**
     * Create a deck of cards from a list of suits and a list of ranks.
     *
     * @param suits the suits
     * @param ranks the ranks
     */
    public Deck(List<Suit> suits, List<Rank> ranks) {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        this.cards = Collections.unmodifiableList(cards);
    }

    public void shuffle() {
        List<Card> cards = new ArrayList<>(this.cards);
        Collections.shuffle(cards);
        this.cards = Collections.unmodifiableList(cards);
    }

    @ReadOnlyCollectionResult
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
