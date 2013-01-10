package com.games.euchre;

import com.games.server.PlayerContainer;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 *
 * A partnership between two players.  A partnership only survives the duration
 * of a single game and it tracks the points earned by the partners.
 */
public final class Partnership {

    /** The first player in the partnership. */
    private final PlayerContainer playerContainer1;

    /** The second player in the partnership. */
    private final PlayerContainer playerContainer2;

    /** The points earned by the partnership. */
    private int points;

    /** The euchres achieved by the partnership. */
    private int euchres;

    /**
     * Creates a partnership for cutthroat Euchre where all players play alone.
     *
     * @param playerContainer the sole player in the partnership
     */
    public Partnership(PlayerContainer playerContainer) {
        this(playerContainer, null);
    }

    /**
     * Create a partnership for non-cutthroat Euchre.
     *
     * @param playerContainer1 the player in the partnership who deals first
     * @param playerContainer2 the second player in the partnership
     */
    public Partnership(PlayerContainer playerContainer1, PlayerContainer playerContainer2) {
        this.playerContainer1 = playerContainer1;
        this.playerContainer2 = playerContainer2;
    }

    public PlayerContainer getPlayerContainer1() {
        return playerContainer1;
    }

    public PlayerContainer getPlayerContainer2() {
        return playerContainer2;
    }

    public boolean contains(PlayerContainer playerContainer) {
        return playerContainer == playerContainer1 || playerContainer == playerContainer2;
    }

    public PlayerContainer getPartner(PlayerContainer playerContainer) {
        assert contains(playerContainer);
        return playerContainer == playerContainer1 ? playerContainer2 : playerContainer1;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getEuchres() {
        return euchres;
    }

    public void addEuchre() {
        euchres++;
    }
}
