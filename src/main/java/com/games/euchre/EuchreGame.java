package com.games.euchre;

import java.util.*;

import com.games.euchre.messages.*;
import com.games.server.*;
import com.games.util.ReadOnlyCollectionResult;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 *
 * Game logic and flow control for a game of Euchre.  Responsible for: ordering
 * players and creating partnerships, managing the dealer, starting hands,
 * controlling the bidding rounds, starting tricks, and scoring.
 */
public class EuchreGame extends Game {

    private EuchreGameOptions gameOptions = EuchreGameOptions.getDefaultGameOptions();

    private List<Player> dealOrder;

    private List<Partnership> partnerships;

    private Player nextDealer;

    private final List<Hand> hands;

    private Hand currentHand;

    private Partnership winner;

    public EuchreGame(String gameId, GameCoordinator gameCoordinator) {
        super(gameId, gameCoordinator);
        hands = new ArrayList<>();
    }

    public EuchreGameOptions getGameOptions() {
        return gameOptions;
    }

    public void setGameOptions(EuchreGameOptions gameOptions) {
        this.gameOptions = gameOptions;
    }

    public boolean hasEnoughPlayers() {
        return players.size() == gameOptions.getNumberOfPlayers();
    }

    @ReadOnlyCollectionResult
    public List<Partnership> getPartnerships() {
        return partnerships;
    }

    public Partnership getPartnership(Player player) {
        assert players.contains(player);
        for (Partnership partnership : partnerships) {
            if (partnership.contains(player)) {
                return partnership;
            }
        }
        throw new AssertionError("player not found in partnerships");
    }

    public Player getPartner(Player player) {
        return getPartnership(player).getPartner(player);
    }

    public void startGame() {

        assert hasEnoughPlayers();

        // randomly determine deal order, which will also determine partnerships
        List<Player> dealOrder = new ArrayList<>(players);
        Collections.shuffle(dealOrder);
        this.dealOrder = Collections.unmodifiableList(dealOrder);
        List<Partnership> partnerships = new ArrayList<>(dealOrder.size() / 2);
        for (int i = 0, n = dealOrder.size() / 2; i < n; i++) {
            partnerships.add(new Partnership(dealOrder.get(i), dealOrder.get(i + n)));
        }
        this.partnerships = Collections.unmodifiableList(partnerships);

        // send notice of deal order and partnerships
        GlobalGameCoordinator.enqueueMessage(new DealOrderSetMessage(this), players);

        // set the first dealer
        nextDealer = dealOrder.get(0);

        try {
            while (winner == null) {

                // start hand
                Hand hand = startNextHand();
                GlobalGameCoordinator.enqueueMessage(new HandStartedMessage(this, hand), getPlayers());

                // deal cards
                hand.dealCards();
                for (Player player : hand.getBidOrder()) {
                    List<Card> dealtCards = hand.getDealtCards(player);
                    GlobalGameCoordinator.enqueueMessage(new CardsDealtMessage(this, hand, dealtCards));
                }

                // first round of bidding
                trumpdeclared:
                for (Player player : hand.getBidOrder()) {
                    ClientMessage message = getNextClientMessage(player, "passBid", "orderDealerUp", "pickCardUp");
                    switch (message.getAction()) {
                        case "passBid":
                            GlobalGameCoordinator.enqueueMessage(new PlayerPassedBidMessage(this, hand));
                            break;
                        case "orderDealerUp":
                            hand.dealerOrderedUp(GlobalGameCoordinator.getPlayer(message.getSessionId()), message.getBooleanAttribute("goingAlone"));
                            //GlobalGameCoordinator.enqueueMessage(new PlayerOrderedDealerUpMessage(this));
                            message = getNextClientMessage(hand.getDealer(), "discardCard");

                            break trumpdeclared;
                        case "pickUpCard":
                            hand.dealerPickedUpCard(message.getBooleanAttribute("goingAlone"));
                            //GlobalGameCoordinator.enqueueMessage(new DealerPickedUpCardMessage());
                            message = getNextClientMessage(hand.getDealer(), "discardCard");


                            break trumpdeclared;
                        default:
                            throw new AssertionError("");
                    }
                }

                // second round of bidding
                if (hand.getTrump() == null) {
                    trumpdeclared:
                    for (Player player : hand.getBidOrder()) {
                        ClientMessage message = getNextClientMessage(player, "passBid", "declareTrump");
                        switch (message.getAction()) {
                            case "passBid":
                                GlobalGameCoordinator.enqueueMessage(new PlayerPassedBidMessage(this, hand));
                                break;
                            case "declareTrump":
                                hand.trumpDeclared(Suit.valueOf(message.getStringAttribute("suit")), GlobalGameCoordinator.getPlayer(message.getSessionId()), message.getBooleanAttribute("goingAlone"));
                                GlobalGameCoordinator.enqueueMessage(new TrumpDeclaredMessage(this, hand));
                                break trumpdeclared;
                        }
                    }
                }

                // play tricks
                cardslayeddown:
                for (int i = 0; i < gameOptions.getTricksPerHand(); i++) {
                    Trick trick = hand.startNextTrick();
                    for (Player player : trick.getPlayOrder()) {
                        ClientMessage message = getNextClientMessage(player, "playCard", "layCardsDown");
                        switch (message.getAction()) {
                            case "playCard":
                                try {
                                    Card card = Card.parse(message.getStringAttribute("card"));
                                    trick.cardPlayed(player, card);
                                    GlobalGameCoordinator.enqueueMessage(new CardPlayedMessage(trick, player, card), players);
                                } catch (DidNotFollowSuitException e) {
                                    // TODO
                                }
                                break;
                            case "layCardsDown":
                                // TODO
                                break cardslayeddown;

                        }
                    }
                    // TODO: send trick finished message

                    // check if more tricks need to be played in order to score the hand
                    if (hand.areAllPointsPlayed()) {
                        break;
                    }
                }

                // score the hand


            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @ReadOnlyCollectionResult
    public List<Player> getDealOrder() {
        return dealOrder;
    }

    /**
     *
     * @return
     */
    private Hand startNextHand() {
        currentHand = new Hand(this, hands.size() + 1, nextDealer);
        hands.add(currentHand);
        return currentHand;
    }

    public Map<Partnership, Integer> getCurrentScore() {
        Map<Partnership, Integer> scores = new LinkedHashMap<Partnership, Integer>();
        // TODO
        return scores;
    }

//    public ServerMessage handleMessage(ClientMessage clientMessage) {
//        switch (clientMessage.getAction()) {
//            case "passBid":
//                return passBid(clientMessage);
//            case "orderDealerUp":
//                return orderDealerUp(clientMessage);
//            case "pickCardUp":
//                return pickCardUp(clientMessage);
//            case "discardCard":
//                return discardCard(clientMessage);
//            case "declareTrump":
//                return declareTrump(clientMessage);
//            case "playCard":
//                return playCard(clientMessage);
//            default:
//                return new InvalidClientMessageMessage(clientMessage, "unknown action");
//        }
//    }
}
