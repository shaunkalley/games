package com.games.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-07
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 *
 * Indicates that all efforts have been made to ensure that the type is
 * immutable.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Immutable {
}
