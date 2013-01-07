package com.games.euchre;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DidNotFollowSuitException extends Exception {

    private final Trick trick;
    private final Card cardPlayed;

    public DidNotFollowSuitException(Trick trick, Card cardPlayed) {
        this.trick = trick;
        this.cardPlayed = cardPlayed;
    }

    public Trick getTrick() {
        return trick;
    }

    public Card cardPlayed() {
        return cardPlayed;
    }
}
