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
 * Various implementations of {@link FloatPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class FloatOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private FloatOps() {}
    ///CLOVER:ON

    
    public static FloatOp abs() {
        return PrimitiveOps.ABS_OP;
    }

    public static FloatReducer add() {
        return PrimitiveOps.ADD_REDUCER;
    }
    
    public static FloatOp add(float add) {
        return new PrimitiveOps.FloatAddOp(add);
    }
        
    public static FloatReducer divide() {
        return PrimitiveOps.DIVIDE_REDUCER;
    }
    
    public static FloatOp divide(float divide) {
        return new PrimitiveOps.FloatDivideOp(divide);
    }
    
    public static FloatReducer multiply() {
        return PrimitiveOps.MULTIPLY_REDUCER;
    }
    
    public static FloatOp multiply(float multiply) {
        return new PrimitiveOps.FloatMultiplyOp(multiply);
    }
    
    public static FloatReducer subtract() {
        return PrimitiveOps.SUBTRACT_REDUCER;
    }
    
    public static FloatOp subtract(float substract) {
        return new PrimitiveOps.FloatSubtractOp(substract);
    }
    /**
     * A comparator for floats relying on natural ordering. The comparator is Serializable.
     */
    public static final FloatComparator COMPARATOR = (FloatComparator) Comparators.NATURAL_COMPARATOR;

    /**
     * A comparator that imposes the reverse of the <i>natural ordering</i> on floats. The
     * comparator is Serializable.
     */
    public static final FloatComparator REVERSE_COMPARATOR = (FloatComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

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
    public static FloatComparator reverseOrder(FloatComparator comparator) {
        return new PrimitiveOps.ReverseFloatComparator(comparator);
    }
        /**
     * A reducer returning the maximum of two float elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final FloatReducer MAX_REDUCER = (FloatReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two float elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final FloatReducer MIN_REDUCER = (FloatReducer) ObjectOps.MIN_REDUCER;
    /**
     * A reducer returning the maximum of two float elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static FloatReducer max(FloatComparator comparator) {
        return new PrimitiveOps.FloatMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two float elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static FloatReducer min(FloatComparator comparator) {
        return new PrimitiveOps.FloatMinReducer(comparator);
    }
 }