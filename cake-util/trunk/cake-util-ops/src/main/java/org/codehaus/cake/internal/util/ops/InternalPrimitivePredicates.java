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
package org.codehaus.cake.internal.util.ops;

import java.io.Serializable;

import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
/**
 * Various implementations of primitive predicates.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PrimitivePredicates.vm 245 2008-12-27 16:17:02Z kasper InternalPrimitivePredicates.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitivePredicates {
    /** Cannot instantiate. */
    private InternalPrimitivePredicates() {}

	    /** A DoublePredicate that performs a logical exclusive AND on two supplied predicates. */
    public static final class AndDoublePredicate implements DoublePredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final DoublePredicate left;

        /** The right side operand. */
        private final DoublePredicate right;

        /**
         * Creates a new <code>AndDoublePredicate</code>.
         *
         * @param left
         *            the left side DoublePredicate
         * @param right
         *            the right side DoublePredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public AndDoublePredicate(DoublePredicate left, DoublePredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(double element) {
            return left.op(element) && right.op(element);
        }

        /**
         * Returns the left side DoublePredicate.
         *
         * @return the left side DoublePredicate.
         */
        public DoublePredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side DoublePredicate.
         *
         * @return the right side DoublePredicate.
         */
        public DoublePredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }
    
    public static class EqualsToDoublePredicate implements DoublePredicate, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final double equalsTo;

        public EqualsToDoublePredicate(double equalsTo) {
            this.equalsTo = equalsTo;
        }

        /**
         * Returns <code>true</code> if the specified value is equal to the value that
         * was used when constructing this predicate, otherwise <code>false</code>.
         *
         * @param t
         *            the value to compare with
         * @return <code>true</code> if the specified value is equal to the value that
         *         was used when constructing this predicate, otherwise <code>false</code>.
         */
        public boolean op(double t) {
            return equalsTo == t;
        }

        /**
         * @return the value we are comparing with
         */
        public double getEqualsTo() {
            return equalsTo;
        }
    }
    
    public static class GreaterThenDoublePredicate implements DoublePredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final double greaterThen;

        public GreaterThenDoublePredicate(double greaterThen) {
            this.greaterThen = greaterThen;
        }

        /** {@inheritDoc} */
        public boolean op(double t) {
            return greaterThen < t;
        }

        public double getGreaterThen() {
            return greaterThen;
        }
    }
    
    public static class GreaterThenOrEqualsDoublePredicate implements DoublePredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final double greaterThenOrEquals;

        public GreaterThenOrEqualsDoublePredicate(double greaterThenOrEquals) {
            this.greaterThenOrEquals = greaterThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(double t) {
            return greaterThenOrEquals <= t;
        }

        public double getGreaterThenOrEquals() {
            return greaterThenOrEquals;
        }
    }
    
    public static class LessThenDoublePredicate implements DoublePredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final double lessThen;

        public LessThenDoublePredicate(double lessThen) {
            this.lessThen = lessThen;
        }

        /** {@inheritDoc} */
        public boolean op(double t) {
            return lessThen > t;
        }

        public double getLessThen() {
            return lessThen;
        }
    }

    public static class LessThenOrEqualsDoublePredicate implements DoublePredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final double lessThenOrEquals;

        public LessThenOrEqualsDoublePredicate(double lessThenOrEquals) {
            this.lessThenOrEquals = lessThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(double t) {
            return lessThenOrEquals >= t;
        }

        public double getLessThenOrEquals() {
            return lessThenOrEquals;
        }
    }
    /**
     * A DoublePredicate that evaluates to true iff the Predicate used for constructing
     * evaluates to <code>false</code>.
     */
    public static final class NotDoublePredicate implements DoublePredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The DoublePredicate to negate. */
        private final DoublePredicate predicate;

        /**
         * Creates a new NotDoublePredicate.
         *
         * @param predicate
         *            the predicate to negate.
         * @throws NullPointerException
         *             if the specified predicate is <code>null</code>
         */
        public NotDoublePredicate(DoublePredicate predicate) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
        }

        /**
         * Returns a boolean representing the logical NOT value of the supplied
         * DoublePredicate.
         *
         * @param element
         *            the element to test
         * @return the logical NOT of the supplied DoublePredicate
         */
        public boolean op(double element) {
            return !predicate.op(element);
        }

        /**
         * Returns the predicate that is being negated.
         *
         * @return the predicate that is being negated.
         */
        public DoublePredicate getPredicate() {
            return predicate;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "!(" + predicate + ")";
        }
    }

    /** A DoublePredicate that performs a logical exclusive OR on two supplied predicates. */
    public static final class OrDoublePredicate implements DoublePredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final DoublePredicate left;

        /** The right side operand. */
        private final DoublePredicate right;

        /**
         * Creates a new <code>OrDoublePredicate</code>.
         *
         * @param left
         *            the left side DoublePredicate
         * @param right
         *            the right side DoublePredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public OrDoublePredicate(DoublePredicate left, DoublePredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(double element) {
            return left.op(element) || right.op(element);
        }

        /**
         * Returns the left side DoublePredicate.
         *
         * @return the left side DoublePredicate.
         */
        public DoublePredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side DoublePredicate.
         *
         * @return the right side DoublePredicate.
         */
        public DoublePredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }    /** A FloatPredicate that performs a logical exclusive AND on two supplied predicates. */
    public static final class AndFloatPredicate implements FloatPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final FloatPredicate left;

        /** The right side operand. */
        private final FloatPredicate right;

        /**
         * Creates a new <code>AndFloatPredicate</code>.
         *
         * @param left
         *            the left side FloatPredicate
         * @param right
         *            the right side FloatPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public AndFloatPredicate(FloatPredicate left, FloatPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(float element) {
            return left.op(element) && right.op(element);
        }

        /**
         * Returns the left side FloatPredicate.
         *
         * @return the left side FloatPredicate.
         */
        public FloatPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side FloatPredicate.
         *
         * @return the right side FloatPredicate.
         */
        public FloatPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }
    
    public static class EqualsToFloatPredicate implements FloatPredicate, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final float equalsTo;

        public EqualsToFloatPredicate(float equalsTo) {
            this.equalsTo = equalsTo;
        }

        /**
         * Returns <code>true</code> if the specified value is equal to the value that
         * was used when constructing this predicate, otherwise <code>false</code>.
         *
         * @param t
         *            the value to compare with
         * @return <code>true</code> if the specified value is equal to the value that
         *         was used when constructing this predicate, otherwise <code>false</code>.
         */
        public boolean op(float t) {
            return equalsTo == t;
        }

        /**
         * @return the value we are comparing with
         */
        public float getEqualsTo() {
            return equalsTo;
        }
    }
    
    public static class GreaterThenFloatPredicate implements FloatPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final float greaterThen;

        public GreaterThenFloatPredicate(float greaterThen) {
            this.greaterThen = greaterThen;
        }

        /** {@inheritDoc} */
        public boolean op(float t) {
            return greaterThen < t;
        }

        public float getGreaterThen() {
            return greaterThen;
        }
    }
    
    public static class GreaterThenOrEqualsFloatPredicate implements FloatPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final float greaterThenOrEquals;

        public GreaterThenOrEqualsFloatPredicate(float greaterThenOrEquals) {
            this.greaterThenOrEquals = greaterThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(float t) {
            return greaterThenOrEquals <= t;
        }

        public float getGreaterThenOrEquals() {
            return greaterThenOrEquals;
        }
    }
    
    public static class LessThenFloatPredicate implements FloatPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final float lessThen;

        public LessThenFloatPredicate(float lessThen) {
            this.lessThen = lessThen;
        }

        /** {@inheritDoc} */
        public boolean op(float t) {
            return lessThen > t;
        }

        public float getLessThen() {
            return lessThen;
        }
    }

    public static class LessThenOrEqualsFloatPredicate implements FloatPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final float lessThenOrEquals;

        public LessThenOrEqualsFloatPredicate(float lessThenOrEquals) {
            this.lessThenOrEquals = lessThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(float t) {
            return lessThenOrEquals >= t;
        }

        public float getLessThenOrEquals() {
            return lessThenOrEquals;
        }
    }
    /**
     * A FloatPredicate that evaluates to true iff the Predicate used for constructing
     * evaluates to <code>false</code>.
     */
    public static final class NotFloatPredicate implements FloatPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The FloatPredicate to negate. */
        private final FloatPredicate predicate;

        /**
         * Creates a new NotFloatPredicate.
         *
         * @param predicate
         *            the predicate to negate.
         * @throws NullPointerException
         *             if the specified predicate is <code>null</code>
         */
        public NotFloatPredicate(FloatPredicate predicate) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
        }

        /**
         * Returns a boolean representing the logical NOT value of the supplied
         * FloatPredicate.
         *
         * @param element
         *            the element to test
         * @return the logical NOT of the supplied FloatPredicate
         */
        public boolean op(float element) {
            return !predicate.op(element);
        }

        /**
         * Returns the predicate that is being negated.
         *
         * @return the predicate that is being negated.
         */
        public FloatPredicate getPredicate() {
            return predicate;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "!(" + predicate + ")";
        }
    }

    /** A FloatPredicate that performs a logical exclusive OR on two supplied predicates. */
    public static final class OrFloatPredicate implements FloatPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final FloatPredicate left;

        /** The right side operand. */
        private final FloatPredicate right;

        /**
         * Creates a new <code>OrFloatPredicate</code>.
         *
         * @param left
         *            the left side FloatPredicate
         * @param right
         *            the right side FloatPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public OrFloatPredicate(FloatPredicate left, FloatPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(float element) {
            return left.op(element) || right.op(element);
        }

        /**
         * Returns the left side FloatPredicate.
         *
         * @return the left side FloatPredicate.
         */
        public FloatPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side FloatPredicate.
         *
         * @return the right side FloatPredicate.
         */
        public FloatPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }    /** A IntPredicate that performs a logical exclusive AND on two supplied predicates. */
    public static final class AndIntPredicate implements IntPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final IntPredicate left;

        /** The right side operand. */
        private final IntPredicate right;

        /**
         * Creates a new <code>AndIntPredicate</code>.
         *
         * @param left
         *            the left side IntPredicate
         * @param right
         *            the right side IntPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public AndIntPredicate(IntPredicate left, IntPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(int element) {
            return left.op(element) && right.op(element);
        }

        /**
         * Returns the left side IntPredicate.
         *
         * @return the left side IntPredicate.
         */
        public IntPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side IntPredicate.
         *
         * @return the right side IntPredicate.
         */
        public IntPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }
    
    public static class EqualsToIntPredicate implements IntPredicate, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final int equalsTo;

        public EqualsToIntPredicate(int equalsTo) {
            this.equalsTo = equalsTo;
        }

        /**
         * Returns <code>true</code> if the specified value is equal to the value that
         * was used when constructing this predicate, otherwise <code>false</code>.
         *
         * @param t
         *            the value to compare with
         * @return <code>true</code> if the specified value is equal to the value that
         *         was used when constructing this predicate, otherwise <code>false</code>.
         */
        public boolean op(int t) {
            return equalsTo == t;
        }

        /**
         * @return the value we are comparing with
         */
        public int getEqualsTo() {
            return equalsTo;
        }
    }
    
    public static class GreaterThenIntPredicate implements IntPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final int greaterThen;

        public GreaterThenIntPredicate(int greaterThen) {
            this.greaterThen = greaterThen;
        }

        /** {@inheritDoc} */
        public boolean op(int t) {
            return greaterThen < t;
        }

        public int getGreaterThen() {
            return greaterThen;
        }
    }
    
    public static class GreaterThenOrEqualsIntPredicate implements IntPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final int greaterThenOrEquals;

        public GreaterThenOrEqualsIntPredicate(int greaterThenOrEquals) {
            this.greaterThenOrEquals = greaterThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(int t) {
            return greaterThenOrEquals <= t;
        }

        public int getGreaterThenOrEquals() {
            return greaterThenOrEquals;
        }
    }
    
    public static class LessThenIntPredicate implements IntPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final int lessThen;

        public LessThenIntPredicate(int lessThen) {
            this.lessThen = lessThen;
        }

        /** {@inheritDoc} */
        public boolean op(int t) {
            return lessThen > t;
        }

        public int getLessThen() {
            return lessThen;
        }
    }

    public static class LessThenOrEqualsIntPredicate implements IntPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final int lessThenOrEquals;

        public LessThenOrEqualsIntPredicate(int lessThenOrEquals) {
            this.lessThenOrEquals = lessThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(int t) {
            return lessThenOrEquals >= t;
        }

        public int getLessThenOrEquals() {
            return lessThenOrEquals;
        }
    }
    /**
     * A IntPredicate that evaluates to true iff the Predicate used for constructing
     * evaluates to <code>false</code>.
     */
    public static final class NotIntPredicate implements IntPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The IntPredicate to negate. */
        private final IntPredicate predicate;

        /**
         * Creates a new NotIntPredicate.
         *
         * @param predicate
         *            the predicate to negate.
         * @throws NullPointerException
         *             if the specified predicate is <code>null</code>
         */
        public NotIntPredicate(IntPredicate predicate) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
        }

        /**
         * Returns a boolean representing the logical NOT value of the supplied
         * IntPredicate.
         *
         * @param element
         *            the element to test
         * @return the logical NOT of the supplied IntPredicate
         */
        public boolean op(int element) {
            return !predicate.op(element);
        }

        /**
         * Returns the predicate that is being negated.
         *
         * @return the predicate that is being negated.
         */
        public IntPredicate getPredicate() {
            return predicate;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "!(" + predicate + ")";
        }
    }

    /** A IntPredicate that performs a logical exclusive OR on two supplied predicates. */
    public static final class OrIntPredicate implements IntPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final IntPredicate left;

        /** The right side operand. */
        private final IntPredicate right;

        /**
         * Creates a new <code>OrIntPredicate</code>.
         *
         * @param left
         *            the left side IntPredicate
         * @param right
         *            the right side IntPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public OrIntPredicate(IntPredicate left, IntPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(int element) {
            return left.op(element) || right.op(element);
        }

        /**
         * Returns the left side IntPredicate.
         *
         * @return the left side IntPredicate.
         */
        public IntPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side IntPredicate.
         *
         * @return the right side IntPredicate.
         */
        public IntPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }    /** A LongPredicate that performs a logical exclusive AND on two supplied predicates. */
    public static final class AndLongPredicate implements LongPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final LongPredicate left;

        /** The right side operand. */
        private final LongPredicate right;

        /**
         * Creates a new <code>AndLongPredicate</code>.
         *
         * @param left
         *            the left side LongPredicate
         * @param right
         *            the right side LongPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public AndLongPredicate(LongPredicate left, LongPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(long element) {
            return left.op(element) && right.op(element);
        }

        /**
         * Returns the left side LongPredicate.
         *
         * @return the left side LongPredicate.
         */
        public LongPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side LongPredicate.
         *
         * @return the right side LongPredicate.
         */
        public LongPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }
    
    public static class EqualsToLongPredicate implements LongPredicate, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final long equalsTo;

        public EqualsToLongPredicate(long equalsTo) {
            this.equalsTo = equalsTo;
        }

        /**
         * Returns <code>true</code> if the specified value is equal to the value that
         * was used when constructing this predicate, otherwise <code>false</code>.
         *
         * @param t
         *            the value to compare with
         * @return <code>true</code> if the specified value is equal to the value that
         *         was used when constructing this predicate, otherwise <code>false</code>.
         */
        public boolean op(long t) {
            return equalsTo == t;
        }

        /**
         * @return the value we are comparing with
         */
        public long getEqualsTo() {
            return equalsTo;
        }
    }
    
    public static class GreaterThenLongPredicate implements LongPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final long greaterThen;

        public GreaterThenLongPredicate(long greaterThen) {
            this.greaterThen = greaterThen;
        }

        /** {@inheritDoc} */
        public boolean op(long t) {
            return greaterThen < t;
        }

        public long getGreaterThen() {
            return greaterThen;
        }
    }
    
    public static class GreaterThenOrEqualsLongPredicate implements LongPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final long greaterThenOrEquals;

        public GreaterThenOrEqualsLongPredicate(long greaterThenOrEquals) {
            this.greaterThenOrEquals = greaterThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(long t) {
            return greaterThenOrEquals <= t;
        }

        public long getGreaterThenOrEquals() {
            return greaterThenOrEquals;
        }
    }
    
    public static class LessThenLongPredicate implements LongPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final long lessThen;

        public LessThenLongPredicate(long lessThen) {
            this.lessThen = lessThen;
        }

        /** {@inheritDoc} */
        public boolean op(long t) {
            return lessThen > t;
        }

        public long getLessThen() {
            return lessThen;
        }
    }

    public static class LessThenOrEqualsLongPredicate implements LongPredicate, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The value to compare with. */
        private final long lessThenOrEquals;

        public LessThenOrEqualsLongPredicate(long lessThenOrEquals) {
            this.lessThenOrEquals = lessThenOrEquals;
        }

        /** {@inheritDoc} */
        public boolean op(long t) {
            return lessThenOrEquals >= t;
        }

        public long getLessThenOrEquals() {
            return lessThenOrEquals;
        }
    }
    /**
     * A LongPredicate that evaluates to true iff the Predicate used for constructing
     * evaluates to <code>false</code>.
     */
    public static final class NotLongPredicate implements LongPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The LongPredicate to negate. */
        private final LongPredicate predicate;

        /**
         * Creates a new NotLongPredicate.
         *
         * @param predicate
         *            the predicate to negate.
         * @throws NullPointerException
         *             if the specified predicate is <code>null</code>
         */
        public NotLongPredicate(LongPredicate predicate) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
        }

        /**
         * Returns a boolean representing the logical NOT value of the supplied
         * LongPredicate.
         *
         * @param element
         *            the element to test
         * @return the logical NOT of the supplied LongPredicate
         */
        public boolean op(long element) {
            return !predicate.op(element);
        }

        /**
         * Returns the predicate that is being negated.
         *
         * @return the predicate that is being negated.
         */
        public LongPredicate getPredicate() {
            return predicate;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "!(" + predicate + ")";
        }
    }

    /** A LongPredicate that performs a logical exclusive OR on two supplied predicates. */
    public static final class OrLongPredicate implements LongPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final LongPredicate left;

        /** The right side operand. */
        private final LongPredicate right;

        /**
         * Creates a new <code>OrLongPredicate</code>.
         *
         * @param left
         *            the left side LongPredicate
         * @param right
         *            the right side LongPredicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public OrLongPredicate(LongPredicate left, LongPredicate right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /** {@inheritDoc} */
        public boolean op(long element) {
            return left.op(element) || right.op(element);
        }

        /**
         * Returns the left side LongPredicate.
         *
         * @return the left side LongPredicate.
         */
        public LongPredicate getLeft() {
            return left;
        }

        /**
         * Returns the right side LongPredicate.
         *
         * @return the right side LongPredicate.
         */
        public LongPredicate getRight() {
            return right;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }
}    