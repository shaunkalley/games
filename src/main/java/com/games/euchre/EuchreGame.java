package com.games.euchre;

import java.util.*;

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

    private final EuchreGameOptions gameOptions;

    private List<PlayerContainer> dealOrder;

    private List<Partnership> partnerships;

    private PlayerContainer nextDealer;

    private final List<Hand> hands;

    private Hand currentHand;

    private Partnership winner;

    public EuchreGame(String gameId, GameCoordinator gameCoordinator, EuchreGameOptions gameOptions) {
        super(gameId, gameCoordinator);
        this.gameOptions = gameOptions;
        hands = new ArrayList<>();
    }

    public EuchreGameOptions getGameOptions() {
        return gameOptions;
    }

    public boolean hasEnoughPlayers() {
        return players.size() == gameOptions.getNumberOfPlayers();
    }

    @ReadOnlyCollectionResult
    public List<Partnership> getPartnerships() {
        return partnerships;
    }

    public Partnership getPartnership(PlayerContainer playerContainer) {
        assert players.contains(playerContainer);
        for (Partnership partnership : partnerships) {
            if (partnership.contains(playerContainer)) {
                return partnership;
            }
        }
        throw new AssertionError("player not found in partnerships");
    }

    public PlayerContainer getPartner(PlayerContainer playerContainer) {
        return getPartnership(playerContainer).getPartner(playerContainer);
    }

    protected void initGame() {

        // randomly determine deal order, which will also determine partnerships
        List<PlayerContainer> dealOrder = new ArrayList<>(players);
        Collections.shuffle(dealOrder);
        this.dealOrder = Collections.unmodifiableList(dealOrder);
        List<Partnership> partnerships = new ArrayList<>(dealOrder.size() / 2);
        for (int i = 0, n = dealOrder.size() / 2; i < n; i++) {
            partnerships.add(new Partnership(dealOrder.get(i), dealOrder.get(i + n)));
        }
        this.partnerships = Collections.unmodifiableList(partnerships);

        // set the first dealer
        nextDealer = dealOrder.get(0);
    }

    protected GameAction getNextAction() {
        // TODO
        return null;
    }

    @ReadOnlyCollectionResult
    public List<PlayerContainer> getDealOrder() {
        return dealOrder;
    }

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
}
