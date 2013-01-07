package com.games.server;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AIPlayer extends Player {

    private static final AtomicLong counter = new AtomicLong(1000);

    public static String nextNickname() {
        return "AIPlayer-" + counter.incrementAndGet();
    }

    private final GameCoordinator coordinator;

    public AIPlayer(GameCoordinator coordinator) {
        super(nextNickname());
        this.coordinator = coordinator;
    }

    public abstract void handleMessage(Message message);
}
