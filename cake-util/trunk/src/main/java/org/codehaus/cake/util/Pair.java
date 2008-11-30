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
package org.codehaus.cake.util;

import org.codehaus.cake.internal.util.CollectionUtils;

/**
 * A <tt>Pair</tt> consists of two references to two objects.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id$
 * @param <T1>
 *            the type of the first operand
 * @param <T2>the
 *            type of the second operand
 * 
 */
public final class Pair<T1, T2> implements java.io.Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The first instance. */
    private final T1 instance1;

    /** The second instance. */
    private final T2 instance2;

    /**
     * Constructs a new Pair from an existing Pair.
     * 
     * @param other
     *            the existing pair
     */
    public Pair(Pair<T1, T2> other) {
        this.instance1 = other.instance1;
        this.instance2 = other.instance2;
    }

    /**
     * Constructs a new Pair. <tt>null</tt> values are permitted.
     * 
     * @param item1
     *            the first item in the Pair
     * @param item2
     *            the second item in the Pair
     */
    public Pair(final T1 item1, final T2 item2) {
        this.instance1 = item1;
        this.instance2 = item2;
    }

    public static <T1, T2> Pair<T1, T2> from(T1 item1, T2 item2) {
        return new Pair<T1, T2>(item1, item2);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other instanceof Pair && equals((Pair<?, ?>) other);
    }

    /**
     * Compare two pairs.
     * 
     * @param other
     *            the other Pair to compare with
     * @return true if the pairs are equal
     */
    public boolean equals(Pair<?, ?> other) {
        return other != null && CollectionUtils.eq(instance1, other.instance1)
                && CollectionUtils.eq(instance2, other.instance2);
    }

    /**
     * Returns the first item in the Pair.
     * 
     * @return the first item in the Pair
     */
    public T1 getFirst() {
        return instance1;
    }

    /**
     * Returns the second item in the Pair.
     * 
     * @return the second item in the Pair
     */
    public T2 getSecond() {
        return instance2;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // we need to calculate the hashCode every time.
        // if we kept the hashCode the hashCode/equals contract might fail.
        return (instance1 == null ? 0 : instance1.hashCode()) ^ (instance2 == null ? 0 : instance2.hashCode());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "{" + instance1 + "," + instance2 + "}";
    }

}
