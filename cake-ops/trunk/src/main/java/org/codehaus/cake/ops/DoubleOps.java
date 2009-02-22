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

import org.codehaus.cake.ops.Ops.DoubleComparator;
import org.codehaus.cake.ops.Ops.DoubleOp;
import org.codehaus.cake.ops.Ops.DoublePredicate;
import org.codehaus.cake.ops.Ops.DoubleReducer;
/**
 * Various implementations of {@link DoublePredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class DoubleOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private DoubleOps() {}
    ///CLOVER:ON

    
    public static DoubleOp abs() {
        return PrimitiveOps.ABS_OP;
    }

    public static DoubleReducer add() {
        return PrimitiveOps.ADD_REDUCER;
    }
    
    public static DoubleOp add(double add) {
        return new PrimitiveOps.DoubleAddOp(add);
    }
        
    public static DoubleReducer divide() {
        return PrimitiveOps.DIVIDE_REDUCER;
    }
    
    public static DoubleOp divide(double divide) {
        return new PrimitiveOps.DoubleDivideOp(divide);
    }
    
    public static DoubleReducer multiply() {
        return PrimitiveOps.MULTIPLY_REDUCER;
    }
    
    public static DoubleOp multiply(double multiply) {
        return new PrimitiveOps.DoubleMultiplyOp(multiply);
    }
    
    public static DoubleReducer subtract() {
        return PrimitiveOps.SUBTRACT_REDUCER;
    }
    
    public static DoubleOp subtract(double substract) {
        return new PrimitiveOps.DoubleSubtractOp(substract);
    }
    /**
     * A comparator for doubles relying on natural ordering. The comparator is Serializable.
     */
    public static final DoubleComparator COMPARATOR = (DoubleComparator) Comparators.NATURAL_COMPARATOR;

    /**
     * A comparator that imposes the reverse of the <i>natural ordering</i> on doubles. The
     * comparator is Serializable.
     */
    public static final DoubleComparator REVERSE_COMPARATOR = (DoubleComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

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
    public static DoubleComparator reverseOrder(DoubleComparator comparator) {
        return new PrimitiveOps.ReverseDoubleComparator(comparator);
    }
        /**
     * A reducer returning the maximum of two double elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final DoubleReducer MAX_REDUCER = (DoubleReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two double elements, using natural comparator.
     * The Reducer is serializable.
     */
     static final DoubleReducer MIN_REDUCER = (DoubleReducer) ObjectOps.MIN_REDUCER;
    /**
     * A reducer returning the maximum of two double elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static DoubleReducer max(DoubleComparator comparator) {
        return new PrimitiveOps.DoubleMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two double elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static DoubleReducer min(DoubleComparator comparator) {
        return new PrimitiveOps.DoubleMinReducer(comparator);
    }
 }