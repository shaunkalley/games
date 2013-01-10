package com.games.server;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-08
 * Time: 11:10 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GameHelper {

    private GameHelper() {
    }

    public static void enqueueServerMessage(PlayerContainer player, ServerMessage message) {
        player.getPlayer().enqueueServerMessage(message);
    }

    public static void enqueueServerMessage(ServerMessage message, PlayerContainer... players) {
        for (PlayerContainer player : players) {
            enqueueServerMessage(player, message);
        }
    }

    public static void enqueueServerMessage(ServerMessage message, Collection<PlayerContainer> players) {
        for (PlayerContainer player : players) {
            enqueueServerMessage(player, message);
        }
    }

    public static ServerMessage getNextServerMessage(PlayerContainer player) throws InterruptedException {
        return player.getPlayer().getNextServerMessage();
    }
}
