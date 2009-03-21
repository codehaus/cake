/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.util.attribute;

import java.util.Comparator;

/**
 * An abstract attribute that can be used to impose order among the values of said attributes. The values can be ordered
 * according to their {@linkplain Comparable natural ordering}, or by a {@link Comparator} provided at attribute
 * construction time, depending on which constructor is used.
 * 
 * An attribute relying on natural ordering does not support comparison of non-comparable objects (doing so may result
 * in {@code ClassCastException}).
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            the datatype of this attribute
 */
public abstract class ComparableObjectAttribute<T> extends ObjectAttribute<T> implements Comparator<AttributeMap> {
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The comparator, or null if attribute uses elements' natural ordering. */
    private final transient Comparator<? super T> comparator;

    /** When comparing whether null is least or greatest. */
    private final transient boolean nullIsLeast;

    /**
     * Creates a new ComparableObjectAttribute with a default value of <code>null</code> and that uses the natural
     * ordering when comparing individual values of this attribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param nullIsLeast
     *            when comparing, whether null is least or greatest
     */
    protected ComparableObjectAttribute(String name, Class<T> clazz, boolean nullIsLeast) {
        super(name, clazz);
        comparator = null;
        if (!Comparable.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("only clazz types assignable from Comparable are allowed");
        }
        this.nullIsLeast = nullIsLeast;
    }

    /**
     * Creates a new ComparableObjectAttribute with a default value of <code>null</code>.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param comparator
     *            the Comparator to use when comparing individual values of this attribute
     */
    protected ComparableObjectAttribute(String name, Class<T> clazz, Comparator<? super T> comparator) {
        super(name, clazz);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
        nullIsLeast = false;// ignore
    }

    /**
     * Creates a new ComparableObjectAttribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param comparator
     *            the Comparator to use when comparing individual values of this attribute
     * @param defaultValue
     *            the default value of the attribute
     */
    protected ComparableObjectAttribute(String name, Class<T> clazz, Comparator<? super T> comparator, T defaultValue) {
        super(name, clazz, defaultValue);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
        nullIsLeast = false;// ignore
    }

    /**
     * Creates a new ComparableObjectAttribute that uses the natural ordering when comparing individual values of this
     * attribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of the attribute
     * @param defaultValue
     *            the default value of the attribute
     * @param nullIsLeast
     *            when comparing whether null is least or greatest
     */
    protected ComparableObjectAttribute(String name, Class<T> clazz, T defaultValue, boolean nullIsLeast) {
        super(name, clazz, defaultValue);
        comparator = null;
        if (!Comparable.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("only clazz types assignable from Comparable are allowed");
        }
        this.nullIsLeast = nullIsLeast;
    }

    /** {@inheritDoc} */
    public int compare(AttributeMap o1, AttributeMap o2) {
        if (comparator == null) {
            Comparable<? super T> thisVal = (Comparable<? super T>) o1.get(this);
            T anotherVal = o2.get(this);
            if (thisVal == null) {
                return anotherVal == null ? 0 : nullIsLeast ? -1 : 1;
            } else if (anotherVal == null) {
                return nullIsLeast ? 1 : -1;
            }
            return thisVal.compareTo(anotherVal);
        } else {
            T thisVal = o1.get(this);
            T anotherVal = o2.get(this);
            return comparator.compare(thisVal, anotherVal);
        }
    }
}
