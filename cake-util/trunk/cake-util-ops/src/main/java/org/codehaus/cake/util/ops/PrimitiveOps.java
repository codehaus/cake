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
/*  This class is automatically generated */ 
package org.codehaus.cake.util.ops;

import static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.ABS_OP;
import static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.ADD_REDUCER;
import static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DIVIDE_REDUCER;
import static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.MULTIPLY_REDUCER;
import static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.SUBTRACT_REDUCER;

import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleAddOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleConstantOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleDivideOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleMaxReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleMinReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleMultiplyOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.DoubleSubtractOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatAddOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatConstantOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatDivideOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatMaxReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatMinReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatMultiplyOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.FloatSubtractOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntAddOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntConstantOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntDivideOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntMaxReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntMinReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntMultiplyOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.IntSubtractOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongAddOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongConstantOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongDivideOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongMaxReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongMinReducer;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongMultiplyOp;
import org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.LongSubtractOp;
import org.codehaus.cake.util.ops.Ops.DoubleComparator;
import org.codehaus.cake.util.ops.Ops.DoubleOp;
import org.codehaus.cake.util.ops.Ops.DoubleReducer;
import org.codehaus.cake.util.ops.Ops.FloatComparator;
import org.codehaus.cake.util.ops.Ops.FloatOp;
import org.codehaus.cake.util.ops.Ops.FloatReducer;
import org.codehaus.cake.util.ops.Ops.IntComparator;
import org.codehaus.cake.util.ops.Ops.IntOp;
import org.codehaus.cake.util.ops.Ops.IntReducer;
import org.codehaus.cake.util.ops.Ops.LongComparator;
import org.codehaus.cake.util.ops.Ops.LongOp;
import org.codehaus.cake.util.ops.Ops.LongReducer;
/**
 * Various implementations of primitive ops.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PrimitivePredicates.vm 245 2008-12-27 16:17:02Z kasper PrimitiveOps.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class PrimitiveOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private PrimitiveOps() {}
    ///CLOVER:ON
    
        /**
     * A reducer returning the maximum of two double elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final DoubleReducer DOUBLE_MAX_REDUCER = (DoubleReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two double elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final DoubleReducer DOUBLE_MIN_REDUCER = (DoubleReducer) ObjectOps.MIN_REDUCER;

    /** An op returning the absolute value. The Reducer is serializable. */
     public static final DoubleOp DOUBLE_ABS_OP = ABS_OP;
     
    /** A reducer returning the sum of two double elements. The Reducer is serializable. */
     public static final DoubleReducer DOUBLE_ADD_REDUCER = ADD_REDUCER;

    /** A reducer returning the sum of two double elements. The Reducer is serializable. */
     public static final DoubleReducer DOUBLE_MULTIPLY_REDUCER = MULTIPLY_REDUCER;

    /** A reducer returning the sum of two double elements. The Reducer is serializable. */
     public static final DoubleReducer DOUBLE_SUBTRACT_REDUCER = SUBTRACT_REDUCER;

    /** A reducer returning the sum of two double elements. The Reducer is serializable. */
     public static final DoubleReducer DOUBLE_DIVISION_REDUCER = DIVIDE_REDUCER;
     
    /**
     * A reducer returning the maximum of two double elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static DoubleReducer doubleMax(DoubleComparator comparator) {
        return new DoubleMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two double elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static DoubleReducer doubleMin(DoubleComparator comparator) {
        return new DoubleMinReducer(comparator);
    }
    
    public static DoubleOp doubleAdd(double add) {
        return new DoubleAddOp(add);
    }
    
    public static DoubleOp doubleDivide(double divide) {
        return new DoubleDivideOp(divide);
    }
    
    public static DoubleOp doubleMultiply(double multiply) {
        return new DoubleMultiplyOp(multiply);
    }
    
    public static DoubleOp doubleSubtract(double substract) {
        return new DoubleSubtractOp(substract);
    }
        
    public static DoubleOp doubleConstant(double constant) {
        return new DoubleConstantOp(constant);
    }    /**
     * A reducer returning the maximum of two float elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final FloatReducer FLOAT_MAX_REDUCER = (FloatReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two float elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final FloatReducer FLOAT_MIN_REDUCER = (FloatReducer) ObjectOps.MIN_REDUCER;

    /** An op returning the absolute value. The Reducer is serializable. */
     public static final FloatOp FLOAT_ABS_OP = ABS_OP;
     
    /** A reducer returning the sum of two float elements. The Reducer is serializable. */
     public static final FloatReducer FLOAT_ADD_REDUCER = ADD_REDUCER;

    /** A reducer returning the sum of two float elements. The Reducer is serializable. */
     public static final FloatReducer FLOAT_MULTIPLY_REDUCER = MULTIPLY_REDUCER;

    /** A reducer returning the sum of two float elements. The Reducer is serializable. */
     public static final FloatReducer FLOAT_SUBTRACT_REDUCER = SUBTRACT_REDUCER;

    /** A reducer returning the sum of two float elements. The Reducer is serializable. */
     public static final FloatReducer FLOAT_DIVISION_REDUCER = DIVIDE_REDUCER;
     
    /**
     * A reducer returning the maximum of two float elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static FloatReducer floatMax(FloatComparator comparator) {
        return new FloatMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two float elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static FloatReducer floatMin(FloatComparator comparator) {
        return new FloatMinReducer(comparator);
    }
    
    public static FloatOp floatAdd(float add) {
        return new FloatAddOp(add);
    }
    
    public static FloatOp floatDivide(float divide) {
        return new FloatDivideOp(divide);
    }
    
    public static FloatOp floatMultiply(float multiply) {
        return new FloatMultiplyOp(multiply);
    }
    
    public static FloatOp floatSubtract(float substract) {
        return new FloatSubtractOp(substract);
    }
        
    public static FloatOp floatConstant(float constant) {
        return new FloatConstantOp(constant);
    }    /**
     * A reducer returning the maximum of two int elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final IntReducer INT_MAX_REDUCER = (IntReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two int elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final IntReducer INT_MIN_REDUCER = (IntReducer) ObjectOps.MIN_REDUCER;

    /** An op returning the absolute value. The Reducer is serializable. */
     public static final IntOp INT_ABS_OP = ABS_OP;
     
    /** A reducer returning the sum of two int elements. The Reducer is serializable. */
     public static final IntReducer INT_ADD_REDUCER = ADD_REDUCER;

    /** A reducer returning the sum of two int elements. The Reducer is serializable. */
     public static final IntReducer INT_MULTIPLY_REDUCER = MULTIPLY_REDUCER;

    /** A reducer returning the sum of two int elements. The Reducer is serializable. */
     public static final IntReducer INT_SUBTRACT_REDUCER = SUBTRACT_REDUCER;

    /** A reducer returning the sum of two int elements. The Reducer is serializable. */
     public static final IntReducer INT_DIVISION_REDUCER = DIVIDE_REDUCER;
     
    /**
     * A reducer returning the maximum of two int elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static IntReducer intMax(IntComparator comparator) {
        return new IntMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two int elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static IntReducer intMin(IntComparator comparator) {
        return new IntMinReducer(comparator);
    }
    
    public static IntOp intAdd(int add) {
        return new IntAddOp(add);
    }
    
    public static IntOp intDivide(int divide) {
        return new IntDivideOp(divide);
    }
    
    public static IntOp intMultiply(int multiply) {
        return new IntMultiplyOp(multiply);
    }
    
    public static IntOp intSubtract(int substract) {
        return new IntSubtractOp(substract);
    }
        
    public static IntOp intConstant(int constant) {
        return new IntConstantOp(constant);
    }    /**
     * A reducer returning the maximum of two long elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final LongReducer LONG_MAX_REDUCER = (LongReducer) ObjectOps.MAX_REDUCER;

    /**
     * A reducer returning the minimum of two long elements, using natural comparator.
     * The Reducer is serializable.
     */
     public static final LongReducer LONG_MIN_REDUCER = (LongReducer) ObjectOps.MIN_REDUCER;

    /** An op returning the absolute value. The Reducer is serializable. */
     public static final LongOp LONG_ABS_OP = ABS_OP;
     
    /** A reducer returning the sum of two long elements. The Reducer is serializable. */
     public static final LongReducer LONG_ADD_REDUCER = ADD_REDUCER;

    /** A reducer returning the sum of two long elements. The Reducer is serializable. */
     public static final LongReducer LONG_MULTIPLY_REDUCER = MULTIPLY_REDUCER;

    /** A reducer returning the sum of two long elements. The Reducer is serializable. */
     public static final LongReducer LONG_SUBTRACT_REDUCER = SUBTRACT_REDUCER;

    /** A reducer returning the sum of two long elements. The Reducer is serializable. */
     public static final LongReducer LONG_DIVISION_REDUCER = DIVIDE_REDUCER;
     
    /**
     * A reducer returning the maximum of two long elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static LongReducer longMax(LongComparator comparator) {
        return new LongMaxReducer(comparator);
    }

    /**
     * A reducer returning the minimum of two long elements, using the specified
     * comparator.
     *
     * @param comparator
     *            the comparator to use when comparing elements
     * @return the newly created reducer
     */
    public static LongReducer longMin(LongComparator comparator) {
        return new LongMinReducer(comparator);
    }
    
    public static LongOp longAdd(long add) {
        return new LongAddOp(add);
    }
    
    public static LongOp longDivide(long divide) {
        return new LongDivideOp(divide);
    }
    
    public static LongOp longMultiply(long multiply) {
        return new LongMultiplyOp(multiply);
    }
    
    public static LongOp longSubtract(long substract) {
        return new LongSubtractOp(substract);
    }
        
    public static LongOp longConstant(long constant) {
        return new LongConstantOp(constant);
    }    
}