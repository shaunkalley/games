package com.games.euchre;

import java.util.*;

import com.games.server.InvalidActionException;
import com.games.server.Player;
import com.games.util.CalledBy;
import com.games.util.ReadOnlyCollectionResult;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 *
 * A hand in a game of euchre.  Tracks the dealer, the selection of trump,
 * whether the hand is a misdeal (i.e., if no trump is selected), what trump
 * is, which partnership is the makers and which is the defenders, and the
 * tricks played.
 */
public final class Hand {

    private final EuchreGame game;

    /** The number of the hand in the game. */
    private final int number;

    /** The dealer for this hand. */
    private final Player dealer;

    private final List<Player> bidOrder;

    /** The cards that where dealt. */
    private Map<Player, List<Card>> dealtCardsMap;

    /** The cards currently held by the players. */
    private Map<Player, List<Card>> playerCardsMap;

    private Card upturnedCard;

    private Suit trump;

    private Player maker;

    private boolean makerGoingAlone;

    private boolean misdeal;

    /** The tricks played in this hand. */
    private final LinkedList<Trick> tricks;

    private Trick currentTrick;

    private Partnership winner;

    public Hand(EuchreGame game, int number, Player dealer) {
        this.game = game;
        this.number = number;
        this.dealer = dealer;

        // create a list of the players in order that they are going to be
        // dealt to and which they will bid in (the dealer will be last)
        List<Player> bidOrder = new ArrayList<>(game.getDealOrder());
        int dealerIndex = bidOrder.indexOf(dealer);
        if (dealerIndex < game.getGameOptions().getNumberOfPlayers() - 1) {
            for (int i = 0; i <= dealerIndex; i++) {
                Player player = bidOrder.remove(0);
                bidOrder.add(player);
            }
        }
        this.bidOrder = Collections.unmodifiableList(bidOrder);

        dealtCardsMap = new LinkedHashMap<>();
        playerCardsMap = new LinkedHashMap<>();
        tricks = new LinkedList<>();
    }

    public EuchreGame getGame() {
        return game;
    }

    public int getNumber() {
        return number;
    }

    public Player getDealer() {
        return dealer;
    }

    @ReadOnlyCollectionResult
    public List<Player> getBidOrder() {
        return bidOrder;
    }

    /**
     * Deals cards from a new, shuffled deck to each player, revealing the
     * card which is going to be bid on for trump.
     */
    public void dealCards() {
        // create a new deck and shuffle it
        Deck deck = new Deck(game.getGameOptions().getSuits(), game.getGameOptions().getRanks());
        deck.shuffle();
        // deal in two rounds, giving either 2 or 3 cards to each player in each round
        Iterator<Card> cards = deck.iterator();
        Random random = new Random(); // use a random-number generator for determining whether to give 2 or 3 cards to the player in the first round of dealing
        for (Player player : bidOrder) {
            List<Card> playerCards = new ArrayList<>(5);
            dealtCardsMap.put(player, playerCards);
            playerCards.add(cards.next());
            playerCards.add(cards.next());
            if (random.nextBoolean()) {
                playerCards.add(cards.next());
            }
        }
        for (Player player : bidOrder) {
            List<Card> playerCards = dealtCardsMap.get(player);
            playerCards.add(cards.next());
            playerCards.add(cards.next());
            if (playerCards.size() == 4) {
                playerCards.add(cards.next());
            }
        }
        upturnedCard = cards.next();
        dealtCardsMap = Collections.unmodifiableMap(dealtCardsMap);
        for (Map.Entry<Player, List<Card>> entry : dealtCardsMap.entrySet()) {
            playerCardsMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    @ReadOnlyCollectionResult
    public List<Card> getDealtCards(Player player) {
        return Collections.unmodifiableList(dealtCardsMap.get(player));
    }

    public Card getUpturnedCard() {
        return upturnedCard;
    }

    // for test purposes only so we can model specific tricks
    void setCards(Player player, List<Card> cards) {
        assert cards.size() == game.getGameOptions().getTricksPerHand();
        playerCardsMap.put(player, cards);
    }

    /**
     * The dealer was ordered up by another player in the first round of
     * bidding.
     *
     * @param maker the player who ordered the dealer up
     * @param makerGoingAlone whether that player opted to go alone or not
     */
    public void dealerOrderedUp(Player maker, boolean makerGoingAlone) {
        trump = upturnedCard.getSuit();
        this.maker = maker;
        this.makerGoingAlone = makerGoingAlone;
    }

    /**
     * The dealer picked up the upturned card in the first round of bidding.
     *
     * @param makerGoingAlone whether the dealer opted to go alone or not
     */
    public void dealerPickedUpCard(boolean makerGoingAlone) {
        trump = upturnedCard.getSuit();
        maker = dealer;
        this.makerGoingAlone = makerGoingAlone;
    }

    /**
     * The dealer discarded a card after being called up in the first round of
     * bidding.
     *
     * @param card the card the dealer discarded
     */
    public void dealerDiscardedCard(Card card) {
        if (card == upturnedCard) {
            throw new InvalidActionException(null); // TODO
        }
        List<Card> dealerCards = playerCardsMap.get(dealer);
        if (!dealerCards.contains(card)) {
            throw new RuntimeException("discarded card not in dealer's hand");
        }
        dealerCards.remove(card);
    }

    /**
     * Trump was declared by a player in the second round of bidding.
     *
     * @param trump the trump suit
     * @param maker the player who called trump
     * @param makerGoingAlone whether the maker is going alone or not
     */
    public void trumpDeclared(Suit trump, Player maker, boolean makerGoingAlone) {
        this.trump = trump;
        this.maker = maker;
        this.makerGoingAlone = makerGoingAlone;
    }

    public Suit getTrump() {
        if (trump == null) {
            throw new IllegalStateException("trump not called yet");
        }
        return trump;
    }

    public Player getMaker() {
        if (trump == null) {
            throw new IllegalStateException("trump not called yet");
        }
        return maker;
    }

    public boolean isMakerGoingAlone() {
        if (trump == null) {
            throw new IllegalStateException("trump not called yet");
        }
        return makerGoingAlone;
    }

    /**
     * Removed a card from a player's hand.
     *
     * @param player the player
     * @param card the card
     */
    @CalledBy(Trick.class)
    public void cardPlayed(Player player, Card card) {
        if (!hasCard(player, card)) {
            throw new AssertionError("card not held by player");
        }
        playerCardsMap.get(player).remove(card);
    }

    @ReadOnlyCollectionResult
    public List<Trick> getTricks() {
        return Collections.unmodifiableList(tricks);
    }

    @CalledBy(EuchreGame.class)
    public Trick startNextTrick() {

        // create a list of the players who are going to take part in the trick
        LinkedList<Player> playOrder = new LinkedList<>(game.getDealOrder());
        if (makerGoingAlone) {
            playOrder.remove(game.getPartner(maker));
        }
        if (tricks.isEmpty()) {
            // first trick so player after dealer leads
            int dealerIndex = playOrder.indexOf(dealer);
            if (dealerIndex < playOrder.size() - 1) {
                for (int i = 0; i <= dealerIndex; i++) {
                    playOrder.addLast(playOrder.removeFirst());
                }
            }
        } else {
            // winner of previous trick leads
            Trick previousTrick = tricks.getLast();
            Player leader = previousTrick.getWinner();
            int leaderIndex = playOrder.indexOf(leader);
            if (leaderIndex != 0) {
                for (int i = 0; i < leaderIndex; i++) {
                    playOrder.addLast(playOrder.removeFirst());
                }
            }
        }

        Trick trick = new Trick(this, Collections.unmodifiableList(playOrder));
        tricks.add(trick);
        currentTrick = trick;
        return trick;
    }

    public boolean hasCard(Player player, Card card) {
        return playerCardsMap.get(player).contains(card);
    }

    public boolean hasSuit(Player player, Suit suit) {
        for (Card card : playerCardsMap.get(player)) {
            if (card.getSuit() == suit) {
                return true;
            }
        }
        return false;
    }

    public int getTricksWon(Partnership partnership) {
        int n = 0;
        for (Trick trick : tricks) {
            if (partnership.contains(trick.getWinner())) {
                n++;
            }
        }
        return n;
    }

    @Override
    public String toString() {
        return "Hand [\n" +

            "]";
    }
}
