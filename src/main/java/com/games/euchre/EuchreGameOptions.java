package com.games.euchre;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.games.util.ReadOnlyCollectionResult;
import org.apache.commons.lang3.ArrayUtils;
import static com.games.euchre.EuchreGameOptions.DeckType.TWENTY_FOUR_CARDS;
import static com.games.euchre.EuchreGameOptions.NumberOfPlayers.FOUR;
import static com.games.euchre.EuchreGameOptions.Scoring.PLAY_TO_10_POINTS;
import static com.games.euchre.EuchreGameOptions.ScoringType.EUCHRES;
import static com.games.euchre.EuchreGameOptions.ScoringType.POINTS;
import static com.games.euchre.Rank.*;

/**
* Created with IntelliJ IDEA.
* User: shaun
* Date: 2013-01-02
* Time: 8:20 AM
* To change this template use File | Settings | File Templates.
*
* Configurable game options including: the number of players (which also
* determines which suits are used in the game), the method of scoring
* (counting points or euchres and the threshold for winning), and whether to
* stick the dealer.
*/
public class EuchreGameOptions {

    public static EuchreGameOptions getDefaultGameOptions() {
        return new EuchreGameOptions();
    }

    /** The number of players (4, 6, or 8). */
    private NumberOfPlayers players = FOUR;

    private DeckType deckType = TWENTY_FOUR_CARDS;

    /** Whether to stick the dealer (force the dealer to call trump if the
     * other players pass on the second round of bidding), or declare a misdeal
     * if trump fails to be called.
     */
    private boolean stickTheDealer = false;

    private Scoring scoring = PLAY_TO_10_POINTS;

    public EuchreGameOptions() {
    }

    public int getNumberOfPlayers() {
        return players.getCount();
    }

    @ReadOnlyCollectionResult
    public List<Suit> getSuits() {
        return players.getSuits();
    }

    public int getTricksPerHand() {
        return deckType.getTricksPerHand();
    }

    @ReadOnlyCollectionResult
    public List<Rank> getRanks() {
        return deckType.getRanks();
    }

    public boolean isStickTheDealer() {
        return stickTheDealer;
    }

    public ScoringType getScoringType() {
        return scoring.getType();
    }

    public int getScoringThreshold() {
        return scoring.getThreshold();
    }

    public enum NumberOfPlayers {

        FOUR(4),
        SIX(6),
        EIGHT(8);

        private final int count;
        private final List<Suit> suits;

        private NumberOfPlayers(int count) {
            this.count = count;
            suits = Collections.unmodifiableList(Arrays.asList(ArrayUtils.subarray(Suit.values(), 0, count)));
        }

        public int getCount() {
            return getCount();
        }

        public List<Suit> getSuits() {
            return suits;
        }
    }

    public enum DeckType {

        TWENTY_FOUR_CARDS(5, ACE, KING, QUEEN, JACK, TEN, NINE),
        THIRTY_TWO_CARDS(7, ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN);

        private final int tricksPerHand;

        private final List<Rank> ranks;

        private DeckType(int tricksPerHand, Rank... ranks) {
            this.tricksPerHand = tricksPerHand;
            this.ranks = Collections.unmodifiableList(Arrays.asList(ranks));
        }

        /**
         * Gets the number of tricks per hand.
         *
         * @return the number of tricks per hand
         */
        public int getTricksPerHand() {
            return tricksPerHand;
        }

        /**
         * Returns a read-only list of the ranks used.
         *
         * @return a read-only list of the ranks used
         */
        public List<Rank> getRanks() {
            return ranks;
        }
    }

    public enum Scoring {

        PLAY_TO_5_POINTS(POINTS, 5),
        PLAY_TO_7_POINTS(POINTS, 7),
        PLAY_TO_10_POINTS(POINTS, 10),
        PLAY_TO_11_POINTS(POINTS, 11),
        PLAY_TO_15_POINTS(POINTS, 15),
        PLAY_TO_3_EUCHRES(EUCHRES, 3),
        PLAY_TO_5_EUCHRES(EUCHRES, 5);

        /** The type of score to keep (points or euchres). */
        private final ScoringType type;

        /** The threshold for winning. */
        private final int threshold;

        private Scoring(ScoringType type, int threshold) {
            this.type = type;
            this.threshold = threshold;
        }

        public ScoringType getType() {
            return type;
        }

        public int getThreshold() {
            return threshold;
        }
    }

    public enum ScoringType {
        POINTS, EUCHRES
    }
}
