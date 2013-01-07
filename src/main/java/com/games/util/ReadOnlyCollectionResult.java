package com.games.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-05
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 *
 * Indicates that the collections-framework return value of a method
 * (Collection, Map, Iterator, etc.) is not modifiable.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ReadOnlyCollectionResult {
}
