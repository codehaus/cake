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

import org.codehaus.cake.ops.Ops.DoubleComparator;
import org.codehaus.cake.ops.Ops.DoubleOp;
import org.codehaus.cake.ops.Ops.DoubleReducer;
import org.codehaus.cake.ops.Ops.FloatComparator;
import org.codehaus.cake.ops.Ops.FloatOp;
import org.codehaus.cake.ops.Ops.FloatReducer;
import org.codehaus.cake.ops.Ops.IntComparator;
import org.codehaus.cake.ops.Ops.IntOp;
import org.codehaus.cake.ops.Ops.IntReducer;
import org.codehaus.cake.ops.Ops.LongComparator;
import org.codehaus.cake.ops.Ops.LongOp;
import org.codehaus.cake.ops.Ops.LongReducer;
import org.codehaus.cake.ops.Ops.PrimitiveOp;
import org.codehaus.cake.ops.Ops.PrimitiveReducer;

/**
 * Various implementations of primitive ops
 * <p>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LongOps.java 229 2008-12-10 19:53:58Z kasper $
 */
final class PrimitiveOps {
    final static PrimitiveAbsOp ABS_OP = new PrimitiveAbsOp();

    final static PrimitiveAddReducer ADD_REDUCER = new PrimitiveAddReducer();

    final static PrimitiveDivideReducer DIVIDE_REDUCER = new PrimitiveDivideReducer();

    final static PrimitiveMultiplyReducer MULTIPLY_REDUCER = new PrimitiveMultiplyReducer();

    final static PrimitiveSubtractReducer SUBTRACT_REDUCER = new PrimitiveSubtractReducer();

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private PrimitiveOps() {
    }

    // /CLOVER:ON

    public static PrimitiveOp abs() {
        return ABS_OP;
    }

    public static PrimitiveReducer add() {
        return ADD_REDUCER;
    }
    public static LongOp add(long add) {
        return null;
    }

    /** A comparator that reserves the result of another DoubleComparator. */
    static final class ReverseDoubleComparator implements DoubleComparator, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The comparator to reverse. */
        private final DoubleComparator comparator;

        /**
         * Creates a new ReverseDoubleComparator.
         * 
         * @param comparator
         *            the comparator to reverse
         */
        ReverseDoubleComparator(DoubleComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(double a, double b) {
            return -comparator.compare(a, b);
        }
    }

    static final class DoubleAddOp implements DoubleOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final double add;

        public DoubleAddOp(double add) {
            this.add = add;
        }

        public double op(double a) {
            return a + add;
        }
    }

    static final class DoubleSubtractOp implements DoubleOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final double subtract;

        public DoubleSubtractOp(double subtract) {
            this.subtract = subtract;
        }

        public double op(double a) {
            return a - subtract;
        }
    }

    static final class DoubleDivideOp implements DoubleOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final double divide;

        public DoubleDivideOp(double divide) {
            this.divide = divide;
        }

        public double op(double a) {
            return a / divide;
        }
    }

    static final class DoubleMultiplyOp implements DoubleOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final double multiply;

        public DoubleMultiplyOp(double multiply) {
            this.multiply = multiply;
        }

        public double op(double a) {
            return a * multiply;
        }
    }

    static final class DoubleConstantOp implements DoubleOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final double constant;

        public DoubleConstantOp(double constant) {
            this.constant = constant;
        }

        public double op(double a) {
            return constant;
        }
    }

    /**
     * A reducer returning the maximum of two double elements, using the given comparator.
     */
    static final class DoubleMaxReducer implements DoubleReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final DoubleComparator comparator;

        /**
         * Creates a DoubleMaxReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        DoubleMaxReducer(DoubleComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public double op(double a, double b) {
            return comparator.compare(a, b) >= 0 ? a : b;
        }
    }

    /**
     * A reducer returning the minimum of two double elements, using the given comparator.
     */
    static final class DoubleMinReducer implements DoubleReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final DoubleComparator comparator;

        /**
         * Creates a DoubleMinReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        DoubleMinReducer(DoubleComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public double op(double a, double b) {
            return comparator.compare(a, b) <= 0 ? a : b;
        }
    }

    /** A comparator that reserves the result of another FloatComparator. */
    static final class ReverseFloatComparator implements FloatComparator, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The comparator to reverse. */
        private final FloatComparator comparator;

        /**
         * Creates a new ReverseFloatComparator.
         * 
         * @param comparator
         *            the comparator to reverse
         */
        ReverseFloatComparator(FloatComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(float a, float b) {
            return -comparator.compare(a, b);
        }
    }

    static final class FloatAddOp implements FloatOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final float add;

        public FloatAddOp(float add) {
            this.add = add;
        }

        public float op(float a) {
            return a + add;
        }
    }

    static final class FloatSubtractOp implements FloatOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final float subtract;

        public FloatSubtractOp(float subtract) {
            this.subtract = subtract;
        }

        public float op(float a) {
            return a - subtract;
        }
    }

    static final class FloatDivideOp implements FloatOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final float divide;

        public FloatDivideOp(float divide) {
            this.divide = divide;
        }

        public float op(float a) {
            return a / divide;
        }
    }

    static final class FloatMultiplyOp implements FloatOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final float multiply;

        public FloatMultiplyOp(float multiply) {
            this.multiply = multiply;
        }

        public float op(float a) {
            return a * multiply;
        }
    }

    static final class FloatConstantOp implements FloatOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final float constant;

        public FloatConstantOp(float constant) {
            this.constant = constant;
        }

        public float op(float a) {
            return constant;
        }
    }

    /**
     * A reducer returning the maximum of two float elements, using the given comparator.
     */
    static final class FloatMaxReducer implements FloatReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final FloatComparator comparator;

        /**
         * Creates a FloatMaxReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        FloatMaxReducer(FloatComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public float op(float a, float b) {
            return comparator.compare(a, b) >= 0 ? a : b;
        }
    }

    /**
     * A reducer returning the minimum of two float elements, using the given comparator.
     */
    static final class FloatMinReducer implements FloatReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final FloatComparator comparator;

        /**
         * Creates a FloatMinReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        FloatMinReducer(FloatComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public float op(float a, float b) {
            return comparator.compare(a, b) <= 0 ? a : b;
        }
    }

    /** A comparator that reserves the result of another IntComparator. */
    static final class ReverseIntComparator implements IntComparator, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The comparator to reverse. */
        private final IntComparator comparator;

        /**
         * Creates a new ReverseIntComparator.
         * 
         * @param comparator
         *            the comparator to reverse
         */
        ReverseIntComparator(IntComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(int a, int b) {
            return -comparator.compare(a, b);
        }
    }

    static final class IntAddOp implements IntOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final int add;

        public IntAddOp(int add) {
            this.add = add;
        }

        public int op(int a) {
            return a + add;
        }
    }

    static final class IntSubtractOp implements IntOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final int subtract;

        public IntSubtractOp(int subtract) {
            this.subtract = subtract;
        }

        public int op(int a) {
            return a - subtract;
        }
    }

    static final class IntDivideOp implements IntOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final int divide;

        public IntDivideOp(int divide) {
            this.divide = divide;
        }

        public int op(int a) {
            return a / divide;
        }
    }

    static final class IntMultiplyOp implements IntOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final int multiply;

        public IntMultiplyOp(int multiply) {
            this.multiply = multiply;
        }

        public int op(int a) {
            return a * multiply;
        }
    }

    static final class IntConstantOp implements IntOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final int constant;

        public IntConstantOp(int constant) {
            this.constant = constant;
        }

        public int op(int a) {
            return constant;
        }
    }

    /**
     * A reducer returning the maximum of two int elements, using the given comparator.
     */
    static final class IntMaxReducer implements IntReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final IntComparator comparator;

        /**
         * Creates a IntMaxReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        IntMaxReducer(IntComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int op(int a, int b) {
            return comparator.compare(a, b) >= 0 ? a : b;
        }
    }

    /**
     * A reducer returning the minimum of two int elements, using the given comparator.
     */
    static final class IntMinReducer implements IntReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final IntComparator comparator;

        /**
         * Creates a IntMinReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        IntMinReducer(IntComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int op(int a, int b) {
            return comparator.compare(a, b) <= 0 ? a : b;
        }
    }

    /** A comparator that reserves the result of another LongComparator. */
    static final class ReverseLongComparator implements LongComparator, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The comparator to reverse. */
        private final LongComparator comparator;

        /**
         * Creates a new ReverseLongComparator.
         * 
         * @param comparator
         *            the comparator to reverse
         */
        ReverseLongComparator(LongComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(long a, long b) {
            return -comparator.compare(a, b);
        }
    }

    static final class LongAddOp implements LongOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final long add;

        public LongAddOp(long add) {
            this.add = add;
        }

        public long op(long a) {
            return a + add;
        }
    }

    static final class LongSubtractOp implements LongOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final long subtract;

        public LongSubtractOp(long subtract) {
            this.subtract = subtract;
        }

        public long op(long a) {
            return a - subtract;
        }
    }

    static final class LongDivideOp implements LongOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final long divide;

        public LongDivideOp(long divide) {
            this.divide = divide;
        }

        public long op(long a) {
            return a / divide;
        }
    }

    static final class LongMultiplyOp implements LongOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final long multiply;

        public LongMultiplyOp(long multiply) {
            this.multiply = multiply;
        }

        public long op(long a) {
            return a * multiply;
        }
    }

    static final class LongConstantOp implements LongOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final long constant;

        public LongConstantOp(long constant) {
            this.constant = constant;
        }

        public long op(long a) {
            return constant;
        }
    }

    /**
     * A reducer returning the maximum of two long elements, using the given comparator.
     */
    static final class LongMaxReducer implements LongReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final LongComparator comparator;

        /**
         * Creates a LongMaxReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        LongMaxReducer(LongComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public long op(long a, long b) {
            return comparator.compare(a, b) >= 0 ? a : b;
        }
    }

    /**
     * A reducer returning the minimum of two long elements, using the given comparator.
     */
    static final class LongMinReducer implements LongReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Comparator used when reducing. */
        private final LongComparator comparator;

        /**
         * Creates a LongMinReducer.
         * 
         * @param comparator
         *            the comparator to use
         */
        LongMinReducer(LongComparator comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public long op(long a, long b) {
            return comparator.compare(a, b) <= 0 ? a : b;
        }
    }

    static final class PrimitiveAbsOp implements PrimitiveOp, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public double op(double a) {
            return Math.abs(a);
        }

        public float op(float a) {
            return Math.abs(a);
        }

        public int op(int a) {
            return Math.abs(a);
        }

        public long op(long a) {
            return Math.abs(a);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ABS_OP;
        }
    }

    static final class PrimitiveAddReducer implements PrimitiveReducer, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public double op(double a, double b) {
            return a + b;
        }

        public float op(float a, float b) {
            return a + b;
        }

        public int op(int a, int b) {
            return a + b;
        }

        public long op(long a, long b) {
            return a + b;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ADD_REDUCER;
        }
    }

    static final class PrimitiveDivideReducer implements DoubleReducer, FloatReducer, IntReducer, LongReducer,
            Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public double op(double a, double b) {
            return a / b;
        }

        public float op(float a, float b) {
            return a / b;
        }

        public int op(int a, int b) {
            return a / b;
        }

        public long op(long a, long b) {
            return a / b;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return DIVIDE_REDUCER;
        }
    }

    static final class PrimitiveMultiplyReducer implements DoubleReducer, FloatReducer, IntReducer, LongReducer,
            Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public double op(double a, double b) {
            return a * b;
        }

        public float op(float a, float b) {
            return a * b;
        }

        public int op(int a, int b) {
            return a * b;
        }

        public long op(long a, long b) {
            return a * b;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MULTIPLY_REDUCER;
        }
    }

    static final class PrimitiveSubtractReducer implements DoubleReducer, FloatReducer, IntReducer, LongReducer,
            Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public double op(double a, double b) {
            return a - b;
        }

        public float op(float a, float b) {
            return a - b;
        }

        public int op(int a, int b) {
            return a - b;
        }

        public long op(long a, long b) {
            return a - b;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SUBTRACT_REDUCER;
        }
    }
}
