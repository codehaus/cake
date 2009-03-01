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
/*  This class is automatically generated */ 
package org.codehaus.cake.ops;

import static org.codehaus.cake.ops.Ops.*;
import static org.codehaus.cake.internal.ops.InternalPrimitivePredicates.*;
/**
 * Various implementations of primitive predicates.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PrimitivePredicates.vm 245 2008-12-27 16:17:02Z kasper PrimitivePredicates.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class PrimitivePredicates {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private PrimitivePredicates() {}
    ///CLOVER:ON
    
    

    /** A DoublePredicate that always evaluates to <code>false</code>. */
    public static final DoublePredicate DOUBLE_FALSE = (DoublePredicate) Predicates.FALSE;

    /** A DoublePredicate that always evaluates to <code>true</code>. */
    public static final DoublePredicate DOUBLE_TRUE = (DoublePredicate) Predicates.TRUE;
    
    /**
     * Creates a DoublePredicate that performs a logical AND on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>false</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) &amp;&amp; right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side DoublePredicate
     * @param right
     *            the right side DoublePredicate
     * @return the newly created DoublePredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static DoublePredicate and(DoublePredicate left, DoublePredicate right) {
        return new AndDoublePredicate(left, right);
    }
    
    /**
     * Creates a predicate that accepts any value that is equal to the value specified.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the value of the equals predicate
     * @return a predicate that accepts any value that is equal to the value specified
     */
    public static DoublePredicate equalsTo(double element) {
        return new EqualsToDoublePredicate(element);
    }
    
    /**
     * Creates a DoublePredicate that evaluates to <code>true</code> if the element being
     * tested is greater then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created DoublePredicate
     */
    public static DoublePredicate greaterThen(double element) {
        return new GreaterThenDoublePredicate(element);
    }

    /**
     * Creates a DoublePredicate that evaluates to <code>true</code> if the element being
     * tested is greater then or equals to the element being used to construct the
     * predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created DoublePredicate
     */
    public static DoublePredicate greaterThenOrEquals(double element) {
        return new GreaterThenOrEqualsDoublePredicate(element);
    }

    /**
     * Creates a DoublePredicate that evaluates to <code>true</code> if the element being
     * tested is less then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created DoublePredicate
     */
    public static DoublePredicate lessThen(double element) {
        return new LessThenDoublePredicate(element);
    }

    /**
     * Creates a DoublePredicate that evaluates to <code>true</code> if the element being
     * tested is less then or equals to the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created DoublePredicate
     */
    public static DoublePredicate lessThenOrEquals(double element) {
        return new LessThenOrEqualsDoublePredicate(element);
    }
    
    /**
     * Creates a DoublePredicate that performs a logical logical NOT on the supplied
     * DoublePredicate. More formally
     *
     * <pre>
     * !predicate.evaluate(value);
     * </pre>
     *
     * <p>
     * If the specified predicate is serializable the returned predicate will also be
     * serializable.
     *
     * @param predicate
     *            the predicate to negate
     * @return the newly created DoublePredicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     */
    public static DoublePredicate not(DoublePredicate predicate) {
        return new NotDoublePredicate(predicate);
    }

    /**
     * Creates a DoublePredicate that performs a logical OR on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>true</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) || right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side DoublePredicate
     * @param right
     *            the right side DoublePredicate
     * @return the newly created DoublePredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static DoublePredicate or(DoublePredicate left, DoublePredicate right) {
        return new OrDoublePredicate(left, right);
    }
 

    /** A FloatPredicate that always evaluates to <code>false</code>. */
    public static final FloatPredicate FLOAT_FALSE = (FloatPredicate) Predicates.FALSE;

    /** A FloatPredicate that always evaluates to <code>true</code>. */
    public static final FloatPredicate FLOAT_TRUE = (FloatPredicate) Predicates.TRUE;
    
    /**
     * Creates a FloatPredicate that performs a logical AND on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>false</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) &amp;&amp; right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side FloatPredicate
     * @param right
     *            the right side FloatPredicate
     * @return the newly created FloatPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static FloatPredicate and(FloatPredicate left, FloatPredicate right) {
        return new AndFloatPredicate(left, right);
    }
    
    /**
     * Creates a predicate that accepts any value that is equal to the value specified.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the value of the equals predicate
     * @return a predicate that accepts any value that is equal to the value specified
     */
    public static FloatPredicate equalsTo(float element) {
        return new EqualsToFloatPredicate(element);
    }
    
    /**
     * Creates a FloatPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created FloatPredicate
     */
    public static FloatPredicate greaterThen(float element) {
        return new GreaterThenFloatPredicate(element);
    }

    /**
     * Creates a FloatPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then or equals to the element being used to construct the
     * predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created FloatPredicate
     */
    public static FloatPredicate greaterThenOrEquals(float element) {
        return new GreaterThenOrEqualsFloatPredicate(element);
    }

    /**
     * Creates a FloatPredicate that evaluates to <code>true</code> if the element being
     * tested is less then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created FloatPredicate
     */
    public static FloatPredicate lessThen(float element) {
        return new LessThenFloatPredicate(element);
    }

    /**
     * Creates a FloatPredicate that evaluates to <code>true</code> if the element being
     * tested is less then or equals to the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created FloatPredicate
     */
    public static FloatPredicate lessThenOrEquals(float element) {
        return new LessThenOrEqualsFloatPredicate(element);
    }
    
    /**
     * Creates a FloatPredicate that performs a logical logical NOT on the supplied
     * FloatPredicate. More formally
     *
     * <pre>
     * !predicate.evaluate(value);
     * </pre>
     *
     * <p>
     * If the specified predicate is serializable the returned predicate will also be
     * serializable.
     *
     * @param predicate
     *            the predicate to negate
     * @return the newly created FloatPredicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     */
    public static FloatPredicate not(FloatPredicate predicate) {
        return new NotFloatPredicate(predicate);
    }

    /**
     * Creates a FloatPredicate that performs a logical OR on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>true</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) || right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side FloatPredicate
     * @param right
     *            the right side FloatPredicate
     * @return the newly created FloatPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static FloatPredicate or(FloatPredicate left, FloatPredicate right) {
        return new OrFloatPredicate(left, right);
    }
 

    /** A IntPredicate that always evaluates to <code>false</code>. */
    public static final IntPredicate INT_FALSE = (IntPredicate) Predicates.FALSE;

    /** A IntPredicate that always evaluates to <code>true</code>. */
    public static final IntPredicate INT_TRUE = (IntPredicate) Predicates.TRUE;
    
    /**
     * Creates a IntPredicate that performs a logical AND on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>false</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) &amp;&amp; right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side IntPredicate
     * @param right
     *            the right side IntPredicate
     * @return the newly created IntPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static IntPredicate and(IntPredicate left, IntPredicate right) {
        return new AndIntPredicate(left, right);
    }
    
    /**
     * Creates a predicate that accepts any value that is equal to the value specified.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the value of the equals predicate
     * @return a predicate that accepts any value that is equal to the value specified
     */
    public static IntPredicate equalsTo(int element) {
        return new EqualsToIntPredicate(element);
    }
    
    /**
     * Creates a IntPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created IntPredicate
     */
    public static IntPredicate greaterThen(int element) {
        return new GreaterThenIntPredicate(element);
    }

    /**
     * Creates a IntPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then or equals to the element being used to construct the
     * predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created IntPredicate
     */
    public static IntPredicate greaterThenOrEquals(int element) {
        return new GreaterThenOrEqualsIntPredicate(element);
    }

    /**
     * Creates a IntPredicate that evaluates to <code>true</code> if the element being
     * tested is less then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created IntPredicate
     */
    public static IntPredicate lessThen(int element) {
        return new LessThenIntPredicate(element);
    }

    /**
     * Creates a IntPredicate that evaluates to <code>true</code> if the element being
     * tested is less then or equals to the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created IntPredicate
     */
    public static IntPredicate lessThenOrEquals(int element) {
        return new LessThenOrEqualsIntPredicate(element);
    }
    
    /**
     * Creates a IntPredicate that performs a logical logical NOT on the supplied
     * IntPredicate. More formally
     *
     * <pre>
     * !predicate.evaluate(value);
     * </pre>
     *
     * <p>
     * If the specified predicate is serializable the returned predicate will also be
     * serializable.
     *
     * @param predicate
     *            the predicate to negate
     * @return the newly created IntPredicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     */
    public static IntPredicate not(IntPredicate predicate) {
        return new NotIntPredicate(predicate);
    }

    /**
     * Creates a IntPredicate that performs a logical OR on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>true</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) || right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side IntPredicate
     * @param right
     *            the right side IntPredicate
     * @return the newly created IntPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static IntPredicate or(IntPredicate left, IntPredicate right) {
        return new OrIntPredicate(left, right);
    }
 

    /** A LongPredicate that always evaluates to <code>false</code>. */
    public static final LongPredicate LONG_FALSE = (LongPredicate) Predicates.FALSE;

    /** A LongPredicate that always evaluates to <code>true</code>. */
    public static final LongPredicate LONG_TRUE = (LongPredicate) Predicates.TRUE;
    
    /**
     * Creates a LongPredicate that performs a logical AND on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>false</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) &amp;&amp; right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side LongPredicate
     * @param right
     *            the right side LongPredicate
     * @return the newly created LongPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static LongPredicate and(LongPredicate left, LongPredicate right) {
        return new AndLongPredicate(left, right);
    }
    
    /**
     * Creates a predicate that accepts any value that is equal to the value specified.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the value of the equals predicate
     * @return a predicate that accepts any value that is equal to the value specified
     */
    public static LongPredicate equalsTo(long element) {
        return new EqualsToLongPredicate(element);
    }
    
    /**
     * Creates a LongPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created LongPredicate
     */
    public static LongPredicate greaterThen(long element) {
        return new GreaterThenLongPredicate(element);
    }

    /**
     * Creates a LongPredicate that evaluates to <code>true</code> if the element being
     * tested is greater then or equals to the element being used to construct the
     * predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created LongPredicate
     */
    public static LongPredicate greaterThenOrEquals(long element) {
        return new GreaterThenOrEqualsLongPredicate(element);
    }

    /**
     * Creates a LongPredicate that evaluates to <code>true</code> if the element being
     * tested is less then the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created LongPredicate
     */
    public static LongPredicate lessThen(long element) {
        return new LessThenLongPredicate(element);
    }

    /**
     * Creates a LongPredicate that evaluates to <code>true</code> if the element being
     * tested is less then or equals to the element being used to construct the predicate.
     * <p>
     * The returned predicate is serializable.
     *
     * @param element
     *            the element to compare with
     * @return the newly created LongPredicate
     */
    public static LongPredicate lessThenOrEquals(long element) {
        return new LessThenOrEqualsLongPredicate(element);
    }
    
    /**
     * Creates a LongPredicate that performs a logical logical NOT on the supplied
     * LongPredicate. More formally
     *
     * <pre>
     * !predicate.evaluate(value);
     * </pre>
     *
     * <p>
     * If the specified predicate is serializable the returned predicate will also be
     * serializable.
     *
     * @param predicate
     *            the predicate to negate
     * @return the newly created LongPredicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     */
    public static LongPredicate not(LongPredicate predicate) {
        return new NotLongPredicate(predicate);
    }

    /**
     * Creates a LongPredicate that performs a logical OR on two supplied predicates. The
     * returned predicate uses short-circuit evaluation (or minimal evaluation). That is,
     * if the specified left side predicate evaluates to <code>true</code> the right
     * side predicate will not be evaluated. More formally
     *
     * <pre>
     * left.evaluate(element) || right.evaluate(element);
     * </pre>
     *
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will
     * also be serializable.
     *
     * @param left
     *            the left side LongPredicate
     * @param right
     *            the right side LongPredicate
     * @return the newly created LongPredicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static LongPredicate or(LongPredicate left, LongPredicate right) {
        return new OrLongPredicate(left, right);
    }
     
}