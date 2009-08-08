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
package org.codehaus.cake.util;

import org.codehaus.cake.internal.util.CollectionUtils;

/**
 * A <tt>Triple</tt> consists of three (final) references to three objects.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id: Triple.java 322 2009-03-21 18:10:43Z kasper $
 * @param <T1>
 *            the type of the first operand
 * @param <T2>the type of the second operand
 * @param <T3>the type of the third operand
 */
public final class Triple<T1, T2, T3> implements java.io.Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The first instance. */
    private final T1 instance1;

    /** The second instance. */
    private final T2 instance2;

    /** The third instance. */
    private final T3 instance3;

    /**
     * Constructs a new Triple from an existing Triple.
     * 
     * @param other
     *            the existing triple
     */
    public Triple(Triple<T1, T2, T3> other) {
        this.instance1 = other.instance1;
        this.instance2 = other.instance2;
        this.instance3 = other.instance3;
    }

    /**
     * Constructs a new Triple. <tt>null</tt> values are permitted.
     * 
     * @param item1
     *            the first item in the Triple
     * @param item2
     *            the second item in the Triple
     */
    public Triple(T1 item1, T2 item2, T3 item3) {
        this.instance1 = item1;
        this.instance2 = item2;
        this.instance3 = item3;
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> from(T1 item1, T2 item2, T3 item3) {
        return new Triple<T1, T2, T3>(item1, item2, item3);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other instanceof Triple<?, ?, ?> && equals((Triple<?, ?, ?>) other);
    }

    /**
     * Compare two triples.
     * 
     * @param other
     *            the other Triple to compare with
     * @return true if the triples are equal
     */
    public boolean equals(Triple<?, ?, ?> other) {
        return other != null && CollectionUtils.eq(instance1, other.instance1)
                && CollectionUtils.eq(instance2, other.instance2) && CollectionUtils.eq(instance3, other.instance3);
    }

    /**
     * Returns the first item in the Triple.
     * 
     * @return the first item in the Triple
     */
    public T1 getFirst() {
        return instance1;
    }

    /**
     * Returns the second item in the Triple.
     * 
     * @return the second item in the Triple
     */
    public T2 getSecond() {
        return instance2;
    }

    /**
     * Returns the third item in the Triple.
     * 
     * @return the third item in the Triple
     */
    public T3 getThird() {
        return instance3;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // we need to calculate the hashCode every time.
        // if we kept the hashCode the hashCode/equals contract might fail.
        return (instance1 == null ? 0 : instance1.hashCode()) ^ (instance2 == null ? 0 : instance2.hashCode())
                ^ (instance3 == null ? 0 : instance3.hashCode());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "{" + instance1 + "," + instance2 + "," + instance3 + "}";
    }

}
