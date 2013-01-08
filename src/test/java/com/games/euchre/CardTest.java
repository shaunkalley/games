package com.games.euchre;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-07
 * Time: 8:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardTest {

    @Test
    public void testToString() {
        Assert.assertEquals("SPADES/ACE", new Card(Suit.SPADES, Rank.ACE).toString());
    }

    @Test
    public void testParse() {
        Assert.assertEquals(new Card(Suit.SPADES, Rank.ACE), Card.parse("SPADES/ACE"));
    }
}
