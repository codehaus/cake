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
package org.codehaus.cake.util.ops;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.internal.util.ops.InternalObjectPredicates;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AllPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AndPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AnyPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.GreaterThenOrEqualPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.GreaterThenPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsEqualsPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsSamePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsTypePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.LessThenOrEqualPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.LessThenPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.MapAndEvaluatePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.NotPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.OrPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.XorPredicate;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

/**
 * Various implementations of {@link Predicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class Predicates {

    /** A Predicate that always evaluates to <code>false</code>. */
    public static final Predicate FALSE = InternalObjectPredicates.FALSE;

    /** A Predicate that returns <code>false</code> if the element being tested is null. */
    public static final Predicate IS_NOT_NULL = InternalObjectPredicates.IS_NOT_NULL;

    /** A Predicate that returns <code>true</code> if the element being tested is null. */
    public static final Predicate IS_NULL = InternalObjectPredicates.IS_NULL;

    /** A Predicate that always evaluates to <code>true</code>. */
    public static final Predicate TRUE = InternalObjectPredicates.TRUE;

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private Predicates() {
    }

    // /CLOVER:ON
    /**
     * As {@link #allTrue(Predicate...)} except taking an {@link Iterable} as parameter.
     * 
     * @param predicates
     *            the predicates to evaluate against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified iterable is <code>null</code> or contains a null element
     */
    public static <E> Predicate<E> allTrue(Iterable<? extends Predicate<? super E>> predicates) {
        return new AllPredicate<E>(predicates);
    }

    /**
     * Creates a Predicate that evaluates to true iff each of the specified predicates evaluates to true for the element
     * being tested. The returned predicate uses short-circuit evaluation (or minimal evaluation). That is, subsequent
     * arguments are only evaluated if the previous arguments does not suffice to determine the truth value.
     * <p>
     * The Predicate will use a copy of the array of supplied predicates.
     * <p>
     * If all the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param predicates
     *            the predicates to test against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static <E> Predicate<E> allTrue(Predicate<? super E>... predicates) {
        return new AllPredicate<E>(predicates);
    }

    /**
     * Creates a Predicate that performs a logical AND on two supplied predicates. The returned predicate uses
     * short-circuit evaluation (or minimal evaluation). That is, if the specified left side predicate evaluates to
     * <code>false</code> the right side predicate will not be evaluated. More formally
     * 
     * <pre>
     * left.evaluate(element) &amp;&amp; right.evaluate(element);
     * </pre>
     * 
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param left
     *            the left side Predicate
     * @param right
     *            the right side Predicate
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static <E> Predicate<E> and(Predicate<? super E> left, Predicate<? super E> right) {
        return new AndPredicate<E>(left, right);
    }

    /**
     * As {@link #anyTrue(Predicate...)} except taking an {@link Iterable} as parameter.
     * 
     * @param predicates
     *            the predicates to evaluate against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified iterable is <code>null</code> or contains a null element
     */
    public static <E> Predicate<E> anyTrue(Iterable<? extends Predicate<? super E>> predicates) {
        return new AnyPredicate<E>(predicates);
    }

    /**
     * Creates a Predicate that evaluates to true if any of the specified predicates evaluates to true for the element
     * being tested. The returned predicate uses short-circuit evaluation (or minimal evaluation). That is, subsequent
     * arguments are only evaluated if the previous arguments does not suffice to determine the truth value.
     * <p>
     * The Predicate will use a copy of the array of supplied predicates.
     * <p>
     * If all the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param predicates
     *            the predicates to test against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static <E> Predicate<E> anyTrue(Predicate<? super E>... predicates) {
        return new AnyPredicate<E>(predicates);
    }

    /**
     * Returns a predicate that tests whether the class of the element being tested is either the same as, or is a
     * superclass or superinterface of, any of the classes or interfaces specified. It returns <code>true</code> if
     * so; otherwise it returns <code>false</code>. This predicate is serializable. The returned predicate uses
     * short-circuit evaluation (or minimal evaluation). That is, subsequent arguments are only evaluated if the
     * previous arguments does not suffice to determine the truth value.
     * <p>
     * The Predicate will use a copy of the array of supplied predicates.
     * <p>
     * If all the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param classes
     *            the types to test against
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if any of the specified classes are <code>null</code>
     * @see #isTypeOf(Class)
     */
    public static Predicate anyType(Class<?>... classes) {
        List<Predicate<?>> list = new ArrayList<Predicate<?>>();
        for (Class<?> c : classes) {
            list.add(isTypeOf(c));
        }
        return anyTrue((List) list);
    }

    /**
     * As {@link #anyType(Class...)} except taking an {@link Iterable} as parameter.
     * 
     * @param classes
     *            the types to test against
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if the specified iterable is <code>null</code> or contains a null element
     * @see #isTypeOf(Class)
     */
    public static Predicate anyType(Iterable<? extends Class<?>> classes) {
        List<Predicate<?>> list = new ArrayList<Predicate<?>>();
        for (Class<?> c : classes) {
            list.add(isTypeOf(c));
        }
        return anyTrue((List) list);
    }

    /**
     * Creates a Predicate that evaluates to true if the element being tested is between the two specified elements
     * (both inclusive) according to the <i>natural ordering</i> of the elements. More formally,
     * 
     * <pre>
     * left &lt;= element &lt;= right
     * </pre>
     * 
     * All elements evaluated must implement the <tt>Comparable</tt> interface. Furthermore, all elements evaluated
     * must be <i>mutually comparable</i> (that is, <tt>left.compareTo(element)</tt> or
     * <tt>right.compareTo(element)</tt> must not throw a <tt>ClassCastException</tt>.
     * <p>
     * If both of the supplied elements are serializable the returned predicate will also be serializable.
     * 
     * @param left
     *            the left-hand element to compare with
     * @param right
     *            the right hand element to compare with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified elements are <code>null</code>
     * @throws IllegalArgumentException
     *             if the specified elements does not implement {@link Comparable}
     * @see #between(Object, Object, Comparator)
     * @see Comparable
     */
    public static <E extends Comparable<? super E>> Predicate<E> between(E left, E right) {
        return and((Predicate) greaterThenOrEqual(left), (Predicate) lessThenOrEqual(right));
    }

    /**
     * As {@link #between(Comparable, Comparable)} except using the specified {@link Comparator} when evaluating
     * elements.
     * 
     * @param left
     *            the left-hand element to compare with
     * @param right
     *            the right hand element to compare with
     * @param comparator
     *            the comparator to compare elements with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified comparator or any of the specified elements are <code>null</code>
     */
    public static <E> Predicate<E> between(E left, E right, Comparator<? extends E> comparator) {
        return and((Predicate) greaterThenOrEqual(left, comparator), (Predicate) lessThenOrEqual(right, comparator));
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> iff the element being evaluated is the same or
     * {@link Object#equals equal} to the element being specified in this method.
     * <p>
     * If the specified object is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to use for comparison
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     * @param <E>
     *            the type of elements accepted by the predicate
     */
    public static <E> Predicate<E> equalsTo(E element) {
        return new IsEqualsPredicate<E>(element);
    }

    /**
     * Creates a Predicate that evaluates to true if any of the specified elements are equal to the element that is
     * being tested. The returned predicate uses short-circuit evaluation (or minimal evaluation). That is, subsequent
     * arguments are only evaluated if the previous arguments does not suffice to determine the truth value.
     * <p>
     * The Predicate will use a copy of the array of supplied predicates.
     * <p>
     * If all the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param elements
     *            the elements to test against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified elements are <code>null</code>
     * @see #equalsTo(Object)
     */
    public static <E> Predicate<E> equalsToAny(E... elements) {
        List<Predicate<E>> list = new ArrayList<Predicate<E>>();
        for (E e : elements) {
            list.add(equalsTo(e));
        }
        return anyTrue(list);
    }

    /**
     * As {@link #equalsToAny(Object...)} except taking an {@link Iterable} as parameter.
     * 
     * @param elements
     *            the elements to test against
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified iterable is <code>null</code> or contains a null element
     * @see #equalsTo(Object)
     */
    public static <E> Predicate<E> equalsToAny(Iterable<? extends E> elements) {
        List<Predicate<E>> list = new ArrayList<Predicate<E>>();
        for (E e : elements) {
            list.add(equalsTo(e));
        }
        return anyTrue(list);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is greater then the element
     * being used to construct the predicate. The predicate will use the objects natural comparator.
     * <p>
     * If the supplied element is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E extends Comparable<? super E>> Predicate<E> greaterThen(E element) {
        return new GreaterThenPredicate<E>(element);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is greater then the element
     * being used to construct the predicate. The predicate will use the specified Comparator to compare the objects.
     * <p>
     * If the supplied element and Comparator is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @param comparator
     *            the Comparator used for comparing elements
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E> Predicate<E> greaterThen(E element, final Comparator<? extends E> comparator) {
        return new GreaterThenPredicate<E>(element, comparator);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is greater then or equal to
     * the element being used to construct the predicate. The predicate will use the objects natural comparator.
     * <p>
     * If the supplied element is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E extends Comparable<? super E>> Predicate<E> greaterThenOrEqual(E element) {
        return new GreaterThenOrEqualPredicate<E>(element);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is greater then or equal to
     * the element being used to construct the predicate. The predicate will use the specified Comparator to compare the
     * objects.
     * <p>
     * If the supplied element and Comparator is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @param comparator
     *            the Comparator used for comparing elements
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E> Predicate<E> greaterThenOrEqual(E element, final Comparator<? extends E> comparator) {
        return new GreaterThenOrEqualPredicate<E>(element, comparator);
    }

    /**
     * Returns a Predicate that always evaluates to <code>false</code>. The returned predicate is serializable.
     * <p>
     * This example illustrates the type-safe way to obtain a false predicate:
     * 
     * <pre>
     * Predicate&lt;String&gt; s = Predicates.falsePredicate();
     * </pre>
     * 
     * Implementation note: Implementations of this method need not create a separate <tt>predicate</tt> object for
     * each call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this
     * method, the field does not provide type safety.)
     * 
     * @see #FALSE
     * @return a Predicate that returns <tt>false</tt> for any element
     * @param <E>
     *            the type of elements accepted by the predicate
     */
    @SuppressWarnings("unchecked")
    public static <E> Predicate<E> isFalse() {
        return FALSE;
    }

    /**
     * Returns a Predicate that returns <code>false</code> if the element being tested is <code>null</code>. This
     * predicate is serializable.
     * <p>
     * Implementation note: Implementations of this method need not create a separate <tt>predicate</tt> object for
     * each call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this
     * method, the field does not provide type safety.)
     * 
     * @param <E>
     *            the types that are accepted by the predicate.
     * @return a Predicate that returns <code>false</code> if the element being tested is <code>null</code>
     */
    public static <E> Predicate<E> isNotNull() {
        return IS_NOT_NULL;
    }

    /**
     * Returns a Predicate that returns <code>true</code> if the element being tested is <code>null</code>. This
     * predicate is serializable.
     * <p>
     * Implementation note: Implementations of this method need not create a separate <tt>predicate</tt> object for
     * each call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this
     * method, the field does not provide type safety.)
     * 
     * @param <E>
     *            the types that are accepted by the predicate.
     * @return a Predicate that returns <code>true</code> if the element being tested is <code>null</code>
     */
    public static <E> Predicate<E> isNull() {
        return IS_NULL;
    }

    /**
     * Creates a Predicate that always evaluates to <code>true</code>. The returned predicate is serializable.
     * <p>
     * This example illustrates the type-safe way to obtain a true predicate:
     * 
     * <pre>
     * Predicate&lt;String&gt; s = Predicates.truePredicate();
     * </pre>
     * 
     * Implementation note: Implementations of this method need not create a separate <tt>predicate</tt> object for
     * each call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this
     * method, the field does not provide type safety.)
     * 
     * @see #TRUE
     * @return a Predicate that returns <tt>true</tt> for any element
     * @param <E>
     *            the type of elements accepted by the predicate
     */
    @SuppressWarnings("unchecked")
    public static <E> Predicate<E> isTrue() {
        return TRUE;
    }

    /**
     * Creates a predicate that tests whether the class of the element being tested is either the same as, or is a
     * superclass or superinterface of, the class or interface represented by the specified Class parameter. It returns
     * <code>true</code> if so; otherwise it returns <code>false</code>. This predicate is serializable.
     * 
     * @param clazz
     *            the class to test
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified clazz is <code>null</code>
     * @throws IllegalArgumentException
     *             if the class represents a primitive type
     * @see Class#isAssignableFrom(Class)
     */
    public static <E> Predicate<E> isTypeOf(Class<?> clazz) {
        return new IsTypePredicate(clazz);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is less then the element
     * being used to construct the predicate. The predicate will use the objects natural comparator.
     * <p>
     * If the supplied element is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E extends Comparable<? super E>> Predicate<E> lessThen(E element) {
        return new LessThenPredicate<E>(element);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is less then the element
     * being used to construct the predicate. The predicate will use the specified Comparator to compare the objects.
     * <p>
     * If the supplied element and Comparator is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @param comparator
     *            the Comparator used for comparing elements
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E> Predicate<E> lessThen(E element, final Comparator<? extends E> comparator) {
        return new LessThenPredicate<E>(element, comparator);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is less then or equal to
     * the element being used to construct the predicate. The predicate will use the objects natural comparator.
     * <p>
     * If the supplied element is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E extends Comparable<? super E>> Predicate<E> lessThenOrEqual(E element) {
        return new LessThenOrEqualPredicate<E>(element);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> if the element being tested is less then or equal to
     * the element being used to construct the predicate. The predicate will use the specified Comparator to compare the
     * objects.
     * <p>
     * If the supplied element and Comparator is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to compare with
     * @param comparator
     *            the Comparator used for comparing elements
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     */
    public static <E> Predicate<E> lessThenOrEqual(E element, final Comparator<? extends E> comparator) {
        return new LessThenOrEqualPredicate<E>(element, comparator);
    }

    /**
     * Creates a Predicate that first applies the specified mapper to the argument before evaluating the specified
     * predicate. More formally
     * 
     * <pre>
     * predicate.evaluate(mapper.map(element));
     * </pre>
     * 
     * <p>
     * If both the supplied mapper and predicate are serializable the returned predicate will also be serializable.
     * 
     * @param mapper
     *            the Mapper that will map the element
     * @param predicate
     *            the Predicate that will evaluate the mapped element
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if either the specified mapper or predicate are <code>null</code>
     * @param <F>
     *            the type of elements accepted by the Predicate being created
     * @param <T>
     *            the type of elements accepted by the specified Predicate
     */
    public static <F, T> Predicate<F> mapAndEvaluate(final Op<F, T> mapper, Predicate<? super T> predicate) {
        return new MapAndEvaluatePredicate<F, T>(mapper, predicate);
    }

    /**
     * Creates a Predicate that performs a logical logical NOT on the supplied Predicate. More formally
     * 
     * <pre>
     * !predicate.evaluate(element);
     * </pre>
     * 
     * <p>
     * If the specified predicate is serializable the returned predicate will also be serializable.
     * 
     * @param predicate
     *            the predicate to negate
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     */
    public static <E> Predicate<E> not(Predicate<? super E> predicate) {
        if (predicate == TRUE) {
            return FALSE;
        } else if (predicate == FALSE) {
            return TRUE;
        } else if (predicate == IS_NULL) {
            return IS_NOT_NULL;
        } else if (predicate == IS_NOT_NULL) {
            return IS_NULL;
        } else if (predicate instanceof NotPredicate) {
            return ((NotPredicate) predicate).getPredicate();
        }
        return new NotPredicate<E>(predicate);
    }

    public static <E> Predicate<E> notEqualsTo(E element) {
        return not(equalsTo(element));
    }

    /**
     * Creates a new Predicate that will evaluate to <code>false</code> if the specified element is <code>null</code>.
     * Otherwise, it will return the evalutation result of the specified predicate evaluate the element. More formally
     * 
     * <pre>
     * element!=null &amp;&amp; predicate.evaluate(element);
     * </pre>
     * 
     * <p>
     * If the specified predicate is serializable the returned predicate will also be serializable.
     * 
     * @param predicate
     *            the predicate
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if the specified predicate is <code>null</code>
     * @param <E>
     *            the type of elements accepted by the predicate
     */
    public static <E> Predicate<E> notNullAnd(Predicate<? super E> predicate) {
        return and(IS_NOT_NULL, predicate);
    }

    /**
     * Creates a Predicate that performs a logical OR on two supplied predicates. The returned predicate uses
     * short-circuit evaluation (or minimal evaluation). That is, if the specified left side predicate evaluates to
     * <code>true</code> the right side predicate will not be evaluated. More formally
     * 
     * <pre>
     * left.evaluate(element) || right.evaluate(element);
     * </pre>
     * 
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param left
     *            the left side Predicate
     * @param right
     *            the right side Predicate
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static <E> Predicate<E> or(Predicate<? super E> left, Predicate<? super E> right) {
        return new OrPredicate<E>(left, right);
    }

    /**
     * Creates a Predicate that evaluates to <code>true</code> iff the element being evaluated has the same object
     * identity as the element being specified in this method.
     * <p>
     * If the specified object is serializable the returned predicate will also be serializable.
     * 
     * @param element
     *            the element to use for comparison
     * @return the newly created Predicate
     * @throws NullPointerException
     *             if the specified element is <code>null</code>
     * @param <E>
     *            the type of elements accepted by the predicate
     */
    public static <E> Predicate<E> sameAs(E element) {
        return new IsSamePredicate<E>(element);
    }

    /**
     * Creates a Predicate that performs a logical logical exclusive OR (XOR) on two supplied predicates. More formally
     * 
     * <pre>
     * left.evaluate(element) &circ; right.evaluate(element);
     * </pre>
     * 
     * <p>
     * If both of the supplied predicates are serializable the returned predicate will also be serializable.
     * 
     * @param left
     *            the left side Predicate
     * @param right
     *            the right side Predicate
     * @return the newly created Predicate
     * @param <E>
     *            the type of elements accepted by the predicate
     * @throws NullPointerException
     *             if any of the specified predicates are <code>null</code>
     */
    public static <E> Predicate<E> xor(Predicate<? super E> left, Predicate<? super E> right) {
        return new XorPredicate<E>(left, right);
    }
}
