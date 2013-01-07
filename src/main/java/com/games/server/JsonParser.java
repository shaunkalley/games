package com.games.server;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-06
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 *
 * Converts an object to and from JSON.
 */
public abstract class JsonParser<T extends JsonParsable> {

    public abstract String toJson(T object);

    public abstract T parseJson(String json) throws Exception;
}
