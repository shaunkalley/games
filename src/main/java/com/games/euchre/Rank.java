package com.games.euchre;

import java.util.Map;

import com.games.server.JsonParsable;
import com.games.server.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 *
 * An enumeration of the card ranks used in standard Euchre: nine through ace,
 * inclusive.
 */
public enum Rank implements JsonParsable {

    ACE("A"),
    KING("K"),
    QUEEN("Q"),
    JACK("J"),
    TEN("10"),
    NINE("9"),
    EIGHT("8"),
    SEVEN("7");

    private static final JsonParser<Rank> jsonParser = new JsonParser<Rank>() {

        @Override
        public String toJson(Rank rank) {
            return "{ \"Rank\": { \"name\": \"" + rank.name() + "\" } }";
        }

        @Override
        public Rank parseJson(String json) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> map = mapper.readValue(json, Map.class);
            Map<String, String> innerMap = map.get("Rank");
            if (innerMap == null) {
                throw new AssertionError("");
            }
            String name = innerMap.get("name");
            if (name == null) {
                throw new AssertionError("");
            }
            return valueOf(name);
        }
    };

    public static JsonParser<Rank> getJsonParser() {
        return jsonParser;
    }

    /** The rank's short name. */
    private final String shortName;

    private Rank(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the short name of this rank.
     *
     * @return the short name
     */
    @Override
    public String toString() {
        return shortName;
    }
}
