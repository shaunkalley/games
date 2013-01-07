package com.games.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 *
 * Helps track flow control by identifying which class calls a method.  Should
 * only be used for methods that are called by one and only one class as part
 * of a specific flow-control sequence.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface CalledBy {

    Class value();
}
