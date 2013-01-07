package com.games.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 *
 * Indicates that a method is an asynchronous callback point.  The required
 * signature of callback methods is:
 * <p>
 * public ServerMessage name(ClientMessage)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Callback {
}
