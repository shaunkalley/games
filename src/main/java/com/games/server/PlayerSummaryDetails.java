package com.games.server;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-10
 * Time: 1:43 AM
 * To change this template use File | Settings | File Templates.
 */
public final class PlayerSummaryDetails {

    private final String sessionId;

    private final String nickname;

    public PlayerSummaryDetails(String sessionId, String nickname) {
        this.sessionId = sessionId;
        this.nickname = nickname;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getNickname() {
        return nickname;
    }
}
