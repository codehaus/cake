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
 * Various implementations of {@link IntPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class IntOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private IntOps() {}
    ///CLOVER:ON

    
    public static IntOp abs() {
        return PrimitiveOps.ABS_OP;
    }

    public static IntReducer add() {
        return PrimitiveOps.ADD_REDUCER;
    }
    
    public static IntOp add(int add) {
        return new PrimitiveOps.IntAddOp(add);
    }
        
    public static IntReducer divide() {
        return PrimitiveOps.DIVIDE_REDUCER;
    }
    
    public static IntOp divide(int divide) {
        return new PrimitiveOps.IntDivideOp(divide);
    }
    
    public static IntReducer multiply() {
        return PrimitiveOps.MULTIPLY_REDUCER;
    }
    
    public static IntOp multiply(int multiply) {
        return new PrimitiveOps.IntMultiplyOp(multiply);
    }
    
    public static IntReducer subtract() {
        return PrimitiveOps.SUBTRACT_REDUCER;
    }
    
    public static IntOp subtract(int substract) {
        return new PrimitiveOps.IntSubtractOp(substract);
    }
    /**
     * A comparator for ints relying on natural ordering. The comparator is Serializable.
     */
    public static final IntComparator COMPARATOR = (IntComparator) Comparators.NATURAL_COMPARATOR;

    /**
     * A comparator that imposes the reverse of the <i>natural ordering</i> on ints. The
     * comparator is Serializable.
     */
    public static final IntComparator REVERSE_COMPARATOR = (IntComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

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
    public static IntComparator reverseOrder(IntComparator comparator) {
        return new PrimitiveOps.ReverseIntComparator(comparator);
    }
        /**
     * A reducer returning the maximum of two int elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final IntReducer MAX_REDUCER = (IntReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two int elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final IntReducer MIN_REDUCER = (IntReducer) ObjectOps.MIN_REDUCER;
    /**
     * A reducer returning the maximum of two int elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static IntReducer max(IntComparator comparator) {
        return new PrimitiveOps.IntMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two int elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static IntReducer min(IntComparator comparator) {
        return new PrimitiveOps.IntMinReducer(comparator);
    }
 }