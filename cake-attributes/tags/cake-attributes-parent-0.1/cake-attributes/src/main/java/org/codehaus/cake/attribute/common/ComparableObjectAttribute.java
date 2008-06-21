/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.attribute.common;

import java.util.Comparator;

import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.attribute.WithAttributes;

/**
 * The attributes ordered according to their
 * {@linkplain Comparable natural ordering}, or by a {@link Comparator}
 * provided at attribute construction time, depending on which constructor is
 * used.
 * 
 * An attribute relying on natural ordering does not permit comparison of
 * non-comparable objects (doing so may result in {@code ClassCastException}).
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 * @param <T>
 *            the datatype of this attribute
 */
public class ComparableObjectAttribute<T> extends ObjectAttribute<T> implements
        Comparator<WithAttributes> {

    /**
     * The comparator, or null if attribute uses elements' natural ordering.
     */
    private final Comparator<? super T> comparator;

    /**
     * Creates a new ComparableObjectAttribute with a default value of
     * <code>null</code> and that uses the natural ordering when comparing
     * individual values of this attribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     */
    public ComparableObjectAttribute(String name, Class<T> clazz) {
        super(name, clazz);
        comparator = null;
    }

    /**
     * Creates a new ComparableObjectAttribute with a default value of
     * <code>null</code>.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param comparator
     *            the Comparator to use when comparing individual values of this
     *            attribute
     */
    public ComparableObjectAttribute(String name, Class<T> clazz,
            Comparator<? super T> comparator) {
        super(name, clazz);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
    }

    /**
     * Creates a new ComparableObjectAttribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param comparator
     *            the Comparator to use when comparing individual values of this
     *            attribute
     * @param defaultValue
     *            the default value of the attribute
     */
    public ComparableObjectAttribute(String name, Class<T> clazz,
            Comparator<? super T> comparator, T defaultValue) {
        super(name, clazz, defaultValue);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
    }

    /**
     * Creates a new ComparableObjectAttribute that uses the natural ordering
     * when comparing individual values of this attribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param defaultValue
     *            the default value of the attribute
     */
    public ComparableObjectAttribute(String name, Class<T> clazz, T defaultValue) {
        super(name, clazz, defaultValue);
        comparator = null;
    }

    /** {@inheritDoc} */
    public int compare(WithAttributes o1, WithAttributes o2) {
        if (comparator == null) {
            Comparable<? super T> thisVal = (Comparable<? super T>) get(o1);
            T anotherVal = (T) get(o2);
            return thisVal.compareTo(anotherVal);
        } else {
            T thisVal = get(o1);
            T anotherVal = get(o2);
            return comparator.compare(thisVal, anotherVal);
        }
    }
}