package com.games.server;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-08
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerContainer {

    private Player player;

    public PlayerContainer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerSummaryDetails getSummaryDetails() {
        return player.getSummaryDetails();
    }
}
