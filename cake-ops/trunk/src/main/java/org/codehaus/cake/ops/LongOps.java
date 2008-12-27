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
package org.codehaus.cake.ops;

import java.io.Serializable;
import static org.codehaus.cake.ops.Ops.*;
import java.math.*;
/**
 * Various implementations of {@link LongPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class LongOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private LongOps() {}
    ///CLOVER:ON

    
    public static LongOp abs() {
        return PrimitiveOps.ABS_OP;
    }

    public static LongReducer add() {
        return PrimitiveOps.ADD_REDUCER;
    }
    
    public static LongOp add(long add) {
        return new PrimitiveOps.LongAddOp(add);
    }
        
    public static LongReducer divide() {
        return PrimitiveOps.DIVIDE_REDUCER;
    }
    
    public static LongOp divide(long divide) {
        return new PrimitiveOps.LongDivideOp(divide);
    }
    
    public static LongReducer multiply() {
        return PrimitiveOps.MULTIPLY_REDUCER;
    }
    
    public static LongOp multiply(long multiply) {
        return new PrimitiveOps.LongMultiplyOp(multiply);
    }
    
    public static LongReducer subtract() {
        return PrimitiveOps.SUBTRACT_REDUCER;
    }
    
    public static LongOp subtract(long substract) {
        return new PrimitiveOps.LongSubtractOp(substract);
    }
    /**
     * A comparator for longs relying on natural ordering. The comparator is Serializable.
     */
    public static final LongComparator COMPARATOR = (LongComparator) Comparators.NATURAL_COMPARATOR;

    /**
     * A comparator that imposes the reverse of the <i>natural ordering</i> on longs. The
     * comparator is Serializable.
     */
    public static final LongComparator REVERSE_COMPARATOR = (LongComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also
     * serializable).
     * 
     * @param comparator
     *            the comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     */
    public static LongComparator reverseOrder(LongComparator comparator) {
        return new PrimitiveOps.ReverseLongComparator(comparator);
    }
        /**
     * A reducer returning the maximum of two long elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final LongReducer MAX_REDUCER = (LongReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two long elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final LongReducer MIN_REDUCER = (LongReducer) ObjectOps.MIN_REDUCER;
    /**
     * A reducer returning the maximum of two long elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static LongReducer max(LongComparator comparator) {
        return new PrimitiveOps.LongMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two long elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static LongReducer min(LongComparator comparator) {
        return new PrimitiveOps.LongMinReducer(comparator);
    }
 }