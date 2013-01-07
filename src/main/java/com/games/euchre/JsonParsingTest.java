package com.games.euchre;

import com.games.server.JsonParser;
import junit.framework.Assert;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonParsingTest {

    @Test
    public void testSuitJsonParsing() {
        Suit suit = Suit.HEARTS;
        JsonParser<Suit> jsonParser = Suit.getJsonParser();
        String json = jsonParser.toJson(suit);
        Assert.assertEquals("{ \"Suit\": { \"name\": \"HEARTS\" } }", json);
        try {
            Suit parsedSuit = jsonParser.parseJson(json);
            Assert.assertEquals(Suit.HEARTS, parsedSuit);
        } catch (Exception e) {
            Assert.fail(ExceptionUtils.getStackTrace(e));
        }
    }

    @Test
    public void testRankJsonParsing() {
        Rank rank = Rank.ACE;
        JsonParser<Rank> jsonParser = Rank.getJsonParser();
        String json = jsonParser.toJson(rank);
        Assert.assertEquals("{ \"Rank\": { \"name\": \"ACE\" } }", json);
        try {
            Rank parsedSuit = jsonParser.parseJson(json);
            Assert.assertEquals(Rank.ACE, parsedSuit);
        } catch (Exception e) {
            Assert.fail(ExceptionUtils.getStackTrace(e));
        }
    }

    @Test
    public void testCardJsonParsing() {

    }
}
