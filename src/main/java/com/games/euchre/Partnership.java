package com.games.euchre;

import com.games.server.Player;

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
    private final Player player1;

    /** The second player in the partnership. */
    private final Player player2;

    /** The points earned by the partners. */
    private int points;

    /** The euchres achieved by the partners. */
    private int euchres;

    /**
     * Creates a partnership for cutthroat Euchre where all players play alone.
     *
     * @param player the sole player in the partnership
     */
    public Partnership(Player player) {
        this(player, null);
    }

    /**
     * Create a partnership for non-cutthroat Euchre.
     *
     * @param player1 the player in the partnership who deals first
     * @param player2 the second player in the partnership
     */
    public Partnership(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean contains(Player player) {
        return player == player1 || player == player2;
    }

    public Player getPartner(Player player) {
        assert contains(player);
        return player == player1 ? player2 : player1;
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
