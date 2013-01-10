package com.games.server;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AIPlayer<T extends Game> extends Player {

    private static final AtomicLong counter = new AtomicLong(1000);

    public static String nextNickname() {
        return "AIPlayer-" + counter.incrementAndGet();
    }

    private final GameCoordinator coordinator;

    private final T game;

    public AIPlayer(GameCoordinator coordinator, T game) {
        super(nextNickname());
        this.coordinator = coordinator;
        this.game = game;
    }
}
