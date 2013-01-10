package com.games.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Player {

    private static String generateSessionId() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    private final String sessionId;

    private final String nickname;

    private final BlockingQueue<ServerMessage> messageQueue;

    public Player(String nickname) {
        sessionId = generateSessionId();
        this.nickname = nickname;
        messageQueue = new LinkedBlockingQueue<>();
    }

    public final String getSessionId() {
        return sessionId;
    }

    public final String getNickname() {
        return nickname;
    }

    public PlayerSummaryDetails getSummaryDetails() {
        return new PlayerSummaryDetails(sessionId, nickname);
    }

    public final void enqueueServerMessage(ServerMessage message) {
        messageQueue.offer(message);
    }

    public final ServerMessage getNextServerMessage() throws InterruptedException {
        return messageQueue.take();
    }

    @Override
    public String toString() {
        return nickname;
    }
}
