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
package org.codehaus.cake.internal.util.ops;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;

public class InternalObjectPredicates {
    /** A Predicate that always evaluates to <code>false</code>. */
    public static final FalsePredicate FALSE = new FalsePredicate();

    /** A Predicate that returns <code>false</code> if the element being tested is null. */
    @SuppressWarnings("unchecked")
    public static final Predicate IS_NOT_NULL = new IsNotNullFilter();

    /** A Predicate that returns <code>true</code> if the element being tested is null. */
    @SuppressWarnings("unchecked")
    public static final Predicate IS_NULL = new IsNullFilter();

    /** A Predicate that always evaluates to <code>true</code>. */
    public static final TruePredicate TRUE = new TruePredicate();

    /** Cannot instantiate. */
    private InternalObjectPredicates() {
    }

    /** A Predicate that tests that <tt>all</tt> of the supplied Predicates accepts a given element. */
    public static final class AllPredicate<E> implements Predicate<E>, Iterable<Predicate<? super E>>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** All the predicates that are being checked. */
        private final Predicate<? super E>[] predicates;

        /**
         * Constructs a new AllPredicate.
         * 
         * @param iterable
         *            the iterable to test
         */
        public AllPredicate(Iterable<? extends Predicate<? super E>> iterable) {
            this.predicates = iterableToArray(iterable);
        }

        /**
         * Constructs a new AllPredicate. The Predicate will use a copy of the array of supplied predicates.
         * 
         * @param predicates
         *            the predicates to test
         */
        @SuppressWarnings("unchecked")
        public AllPredicate(final Predicate<? super E>[] predicates) {
            this.predicates = new Predicate[predicates.length];
            System.arraycopy(predicates, 0, this.predicates, 0, predicates.length);
            for (int i = 0; i < this.predicates.length; i++) {
                if (this.predicates[i] == null) {
                    throw new NullPointerException("predicates contained a null on index = " + i);
                }
            }
        }

        /**
         * Returns the predicates we are testing against.
         * 
         * @return the predicates we are testing against
         */
        public List<Predicate<? super E>> getPredicates() {
            return Collections.unmodifiableList(Arrays.asList(predicates));
        }

        /** {@inheritDoc} */
        public Iterator<Predicate<? super E>> iterator() {
            return Arrays.asList(predicates).iterator();
        }

        /**
         * Returns <tt>true</tt> if all supplied Predicates accepts the element.
         * 
         * @param element
         *            the element to test
         * @return <tt>true</tt> if all supplied Predicates accepts the element.
         */
        public boolean op(E element) {
            for (Predicate<? super E> predicate : predicates) {
                if (!predicate.op(element)) {
                    return false;
                }
            }
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            if (predicates.length == 0) {
                return "";
            } else if (predicates.length == 1) {
                return predicates[0].toString();
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("((");
                builder.append(predicates[0]);
                builder.append(")");
                for (int i = 1; i < predicates.length; i++) {
                    builder.append(" and (");
                    builder.append(predicates[i]);
                    builder.append(")");
                }
                builder.append(")");
                return builder.toString();
            }
        }
    }

    /** A Predicate that performs a logical exclusive AND on two supplied predicates. */
    public static final class AndPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final Predicate<? super E> left;

        /** The right side operand. */
        private final Predicate<? super E> right;

        /**
         * Creates a new <code>AndPredicate</code>.
         * 
         * @param left
         *            the left side Predicate
         * @param right
         *            the right side Predicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public AndPredicate(Predicate<? super E> left, Predicate<? super E> right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /**
         * Returns the left side Predicate.
         * 
         * @return the left side Predicate.
         */
        public Predicate<? super E> getLeftPredicate() {
            return left;
        }

        /**
         * Returns the right side Predicate.
         * 
         * @return the right side Predicate.
         */
        public Predicate<? super E> getRightPredicate() {
            return right;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return left.op(element) && right.op(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") && (" + right + ")";
        }
    }

    /** A Predicate that tests that at least one of the supplied predicates accepts a given element. */
    public static final class AnyPredicate<E> implements Predicate<E>, Iterable<Predicate<? super E>>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** All the predicates that are being checked. */
        private final Predicate<? super E>[] predicates;

        /**
         * Constructs a new AllPredicate.
         * 
         * @param iterable
         *            the iterable to test
         */
        public AnyPredicate(Iterable<? extends Predicate<? super E>> iterable) {
            this.predicates = iterableToArray(iterable);
        }

        /**
         * Constructs a new AllPredicate. The Predicate will use a copy of the array of supplied predicates.
         * 
         * @param predicates
         *            the predicates to test
         */
        @SuppressWarnings("unchecked")
        public AnyPredicate(final Predicate<? super E>[] predicates) {
            this.predicates = new Predicate[predicates.length];
            System.arraycopy(predicates, 0, this.predicates, 0, predicates.length);
            for (int i = 0; i < this.predicates.length; i++) {
                if (this.predicates[i] == null) {
                    throw new NullPointerException("predicates contained a null on index = " + i);
                }
            }
        }

        /**
         * Returns the predicates we are testing against.
         * 
         * @return the predicates we are testing against
         */
        public List<Predicate<? super E>> getPredicates() {
            return Collections.unmodifiableList(Arrays.asList(predicates));
        }

        /** {@inheritDoc} */
        public Iterator<Predicate<? super E>> iterator() {
            return Arrays.asList(predicates).iterator();
        }

        /**
         * Returns <tt>true</tt> if all supplied Predicates accepts the element.
         * 
         * @param element
         *            the element to test
         * @return <tt>true</tt> if all supplied Predicates accepts the element.
         */
        public boolean op(E element) {
            for (Predicate<? super E> predicate : predicates) {
                if (predicate.op(element)) {
                    return true;
                }
            }
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            if (predicates.length == 0) {
                return "";
            } else if (predicates.length == 1) {
                return predicates[0].toString();
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("((");
                builder.append(predicates[0]);
                builder.append(")");
                for (int i = 1; i < predicates.length; i++) {
                    builder.append(" or (");
                    builder.append(predicates[i]);
                    builder.append(")");
                }
                builder.append(")");
                return builder.toString();
            }
        }
    }

    /** A Predicate that always evaluates to <tt>false</tt>. Use {@link #FALSE} to get an instance of this Predicate. */
    public static final class FalsePredicate implements Predicate<Object>, BytePredicate, CharPredicate,
            DoublePredicate, FloatPredicate, IntPredicate, LongPredicate, ShortPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        FalsePredicate() {
        }

        /** {@inheritDoc} */
        public boolean op(byte a) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(char a) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(double a) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(float a) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(int a) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(long element) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(Object element) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean op(short a) {
            return false;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return FALSE;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return Boolean.FALSE.toString();
        }
    }

    /** A Greater Then Or Equal predicate as per Comparable/Comparator contract. */
    public static final class GreaterThenOrEqualPredicate<E> implements Predicate<E>, Serializable {

        /** <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /**
         * The comparator to compare elements with or null if the objects natural comparator should be used.
         */
        private final Comparator comparator;

        /** The object to compare against. */
        private final Object object;

        /**
         * Creates a new greater then predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param comparator
         *            the comparator that should be used to compare elements
         */
        public GreaterThenOrEqualPredicate(E object, final Comparator<? extends E> comparator) {
            if (object == null) {
                throw new NullPointerException("element is null");
            } else if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.object = object;
            this.comparator = comparator;
        }

        /**
         * Creates a new greater then predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param <T>
         *            type of objects accepted by the predicate
         */
        public <T extends Comparable<? super E>> GreaterThenOrEqualPredicate(T object) {
            if (object == null) {
                throw new NullPointerException("element is null");
            }
            this.object = object;
            this.comparator = null;
        }

        /**
         * @return the comparator to compare elements with or null if the objects natural comparator should be used.
         */
        public Comparator<? extends E> getComparator() {
            return comparator;
        }

        /**
         * Returns the object we are comparing.
         * 
         * @return the object we are comparing
         */
        public E getObject() {
            return (E) object;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public boolean op(E element) {
            return (comparator == null ? ((Comparable) object).compareTo(element) : comparator.compare(object, element)) <= 0;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return " >= " + object;
        }
    }

    /** A greater-then predicate as per Comparable/Comparator contract. */
    public static final class GreaterThenPredicate<E> implements Predicate<E>, Serializable {

        /** <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The comparator to compare elements with or null if the objects natural comparator should be used. */
        private final Comparator comparator;

        /** The object to compare against. */
        private final Object object;

        /**
         * Creates a new greater then predicate.
         * 
         * @param object
         *            the objetc to compare with.
         * @param comparator
         *            the comparator that should be used to compare elements
         */
        public GreaterThenPredicate(E object, final Comparator<? extends E> comparator) {
            if (object == null) {
                throw new NullPointerException("element is null");
            } else if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.object = object;
            this.comparator = comparator;
        }

        /**
         * Creates a new greater then predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param <T>
         *            type of objects accepted by the predicate
         */
        public <T extends Comparable<? super E>> GreaterThenPredicate(T object) {
            if (object == null) {
                throw new NullPointerException("element is null");
            }
            this.object = object;
            this.comparator = null;
        }

        /**
         * @return the comparator to compare elements with or null if the objects natural comparator should be used.
         */
        public Comparator<? extends E> getComparator() {
            return comparator;
        }

        /**
         * Returns the object we are comparing.
         * 
         * @return the object we are comparing
         */
        public E getObject() {
            return (E) object;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public boolean op(E element) {
            return (comparator == null ? ((Comparable) object).compareTo(element) : comparator.compare(object, element)) < 0;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "$x > " + object;
        }
    }

    /**
     * A Predicate that evaluates to <code>true</code> iff the element being evaluated is {@link Object#equals equal} to
     * the element being specified.
     */
    public static final class IsEqualsPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = -802615306772905787L;

        /** The element to compare with. */
        private final E element;

        /**
         * Creates an IsEqualsPredicate.
         * 
         * @param element
         *            the element to use for comparison
         * @throws NullPointerException
         *             if the specified element is <code>null</code>
         */
        public IsEqualsPredicate(E element) {
            if (element == null) {
                throw new NullPointerException("element is null");
            }
            this.element = element;
        }

        /**
         * Returns the element we are comparing with.
         * 
         * @return the element we are comparing with
         */
        public E getElement() {
            return element;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return this.element == element || this.element.equals(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "equals " + element;
        }
    }

    /** A Predicate that returns <code>true</code> if the element being tested is not null. */
    public static final class IsNotNullFilter implements Predicate<Object>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        IsNotNullFilter() {
        }

        /** {@inheritDoc} */
        public boolean op(Object element) {
            return element != null;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IS_NOT_NULL;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "is not null";
        }
    }

    /**
     * A Predicate that returns <code>true</code> if the element being tested is null.
     */
    public static final class IsNullFilter implements Predicate<Object>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        IsNullFilter() {
        }

        /** {@inheritDoc} */
        public boolean op(Object element) {
            return element == null;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IS_NULL;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "is null";
        }
    }

    /**
     * A Predicate that evaluates to <code>true</code> iff the element being evaluated has the same object identity as
     * the element being specified.
     */
    public static final class IsSamePredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The element to compare with. */
        private final E element;

        /**
         * Creates an IsSamePredicate.
         * 
         * @param element
         *            the element to use for comparison
         * @throws NullPointerException
         *             if the specified element is <code>null</code>
         */
        public IsSamePredicate(E element) {
            if (element == null) {
                throw new NullPointerException("element is null");
            }
            this.element = element;
        }

        /**
         * Returns the element we are comparing with.
         * 
         * @return the element we are comparing with
         */
        public E getElement() {
            return element;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return this.element == element;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "is (==) " + element;
        }
    }

    /**
     * A predicate that tests whether the class of the element being tested is either the same as, or is a superclass or
     * superinterface of, the class or interface represented by the specified Class parameter. It returns
     * <code>true</code> if so; otherwise it returns <code>false</code>.
     */
    public static final class IsTypePredicate<E> implements Predicate<E>, Serializable {

        /** A default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The class we are testing against. */
        private final Class<?> theClass;

        /**
         * Creates a new IsTypePredicate.
         * 
         * @param theClass
         *            the class we are testing against.
         * @throws NullPointerException
         *             if the specified clazz is <code>null</code>
         * @throws IllegalArgumentException
         *             if the class represents a primitive type
         */
        public IsTypePredicate(Class<?> theClass) {
            if (theClass == null) {
                throw new NullPointerException("theClass is null");
            } else if (theClass.isPrimitive()) {
                throw new IllegalArgumentException("cannot create IsTypePredicate from primitive class '"
                        + theClass.getName() + "', since all primitive arguments to evaluate() are automatically boxed");
            }

            this.theClass = theClass;
        }

        /**
         * Returns the class we are testing against.
         * 
         * @return Returns the theClass.
         */
        public Class<?> getType() {
            return theClass;
        }

        /**
         * Tests the given element for acceptance.
         * 
         * @param element
         *            the element to test against.
         * @return <code>true</code> if the filter accepts the element; <code>false</code> otherwise.
         */
        public boolean op(E element) {
            return theClass.isAssignableFrom(element.getClass());
        }
    }

    /** A Less Then predicate as per Comparable/Comparator contract. */
    public static final class LessThenOrEqualPredicate<E> implements Predicate<E>, Serializable {

        /** <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /**
         * The comparator to compare elements with or null if the objects natural comparator should be used.
         */
        private final Comparator comparator;

        /** The object to compare against. */
        private final Object object;

        /**
         * Creates a new less then or equal predicate.
         * 
         * @param object
         *            the objetc to compare with.
         * @param comparator
         *            the comparator that should be used to compare elements
         */
        public LessThenOrEqualPredicate(E object, final Comparator<? extends E> comparator) {
            if (object == null) {
                throw new NullPointerException("element is null");
            } else if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.object = object;
            this.comparator = comparator;
        }

        /**
         * Creates a new less then or equals predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param <T>
         *            type of objects accepted by the predicate
         */
        public <T extends Comparable<? super E>> LessThenOrEqualPredicate(T object) {
            if (object == null) {
                throw new NullPointerException("element is null");
            }
            this.object = object;
            this.comparator=null;
        }

        /**
         * @return the comparator to compare elements with or null if the objects natural comparator should be used.
         */
        public Comparator<? extends E> getComparator() {
            return comparator;
        }

        /**
         * Returns the object we are comparing.
         * 
         * @return the object we are comparing
         */
        public E getObject() {
            return (E) object;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public boolean op(E element) {
            return (comparator == null ? ((Comparable) object).compareTo(element) : comparator.compare(object, element)) >= 0;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return " <= " + object;
        }
    }

    /** A Less Then predicate as per Comparable/Comparator contract. */
    public static final class LessThenPredicate<E> implements Predicate<E>, Serializable {

        /** <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /**
         * The comparator to compare elements with or null if the objects natural comparator should be used.
         */

        private final Comparator comparator;

        /** The object to compare against. */
        private final Object object;

        /**
         * Creates a new less then Predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param comparator
         *            the comparator that should be used to compare elements
         */
        public LessThenPredicate(E object, final Comparator<? extends E> comparator) {
            if (object == null) {
                throw new NullPointerException("element is null");
            } else if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.object = object;
            this.comparator = comparator;
        }

        /**
         * Creates a new less then predicate.
         * 
         * @param object
         *            the object to compare with.
         * @param <T>
         *            type of objects accepted by the predicate
         */
        public <T extends Comparable<? super E>> LessThenPredicate(T object) {
            if (object == null) {
                throw new NullPointerException("element is null");
            }
            this.object = object;
            this.comparator = null;
        }

        /**
         * @return the comparator to compare elements with or null if the objects natural comparator should be used.
         */
        public Comparator<? extends E> getComparator() {
            return comparator;
        }

        /**
         * Returns the object we are comparing.
         * 
         * @return the object we are comparing
         */
        public E getObject() {
            return (E) object;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public boolean op(E element) {
            return (comparator == null ? ((Comparable) object).compareTo(element) : comparator.compare(object, element)) > 0;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "$x < " + object;
        }
    }

    /**
     * A Predicate that first applies the specified mapper to the argument before evaluating the specified predicate.
     */
    public static final class MapAndEvaluatePredicate<F, T> implements Predicate<F>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -6292758840373110577L;

        /** The mapper used to map the element. */
        private final Op<F, T> mapper;

        /** The predicate to test the mapped value against. */
        private final Predicate<? super T> predicate;

        /**
         * Creates a new MapAndEvaluatePredicate.
         * 
         * @param mapper
         *            the mapper used to first map the argument
         * @param predicate
         *            the predicate used to evaluate the mapped argument
         */
        public MapAndEvaluatePredicate(Op<F, T> mapper, Predicate<? super T> predicate) {
            if (mapper == null) {
                throw new NullPointerException("mapper is null");
            } else if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
            this.mapper = mapper;
        }

        /**
         * Returns the mapper that will map the object before applying the predicate on it.
         * 
         * @return the mapper that will map the object before applying the predicate on it
         */
        public Op<F, T> getMapper() {
            return mapper;
        }

        /**
         * Returns the Predicate we are testing against.
         * 
         * @return the Predicate we are testing against.
         */
        public Predicate<? super T> getPredicate() {
            return predicate;
        }

        /**
         * Accepts all elements that are {@link Object#equals equal} to the specified object.
         * 
         * @param element
         *            the element to test against.
         * @return <code>true</code> if the predicate accepts the element; <code>false</code> otherwise.
         */
        public boolean op(F element) {
            return predicate.op(mapper.op(element));
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "convert " + mapper;
        }
    }

    /**
     * A Predicate that evaluates to true iff the Predicate used for constructing evaluates to <code>false</code>.
     */
    public static final class NotPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The Predicate to negate. */
        private final Predicate<? super E> predicate;

        /**
         * Creates a new NotPredicate.
         * 
         * @param predicate
         *            the predicate to negate.
         * @throws NullPointerException
         *             if the specified predicate is <code>null</code>
         */
        public NotPredicate(Predicate<? super E> predicate) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
        }

        /**
         * Returns the predicate that is being negated.
         * 
         * @return the predicate that is being negated.
         */
        public Predicate<? super E> getPredicate() {
            return predicate;
        }

        /**
         * Returns a boolean representing the logical NOT value of the supplied Predicate.
         * 
         * @param element
         *            the element to test
         * @return the logical NOT of the supplied Predicate
         */
        public boolean op(E element) {
            return !predicate.op(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "!(" + predicate + ")";
        }
    }

    /**
     * A Predicate that performs a logical inclusive OR on two supplied predicates.
     */
    public static final class OrPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final Predicate<? super E> left;

        /** The right side operand. */
        private final Predicate<? super E> right;

        /**
         * Creates a new <code>OrPredicate</code>.
         * 
         * @param left
         *            the left side Predicate
         * @param right
         *            the right side Predicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public OrPredicate(Predicate<? super E> left, Predicate<? super E> right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /**
         * Returns the left side Predicate.
         * 
         * @return the left side Predicate.
         */
        public Predicate<? super E> getLeftPredicate() {
            return left;
        }

        /**
         * Returns the right side Predicate.
         * 
         * @return the right side Predicate.
         */
        public Predicate<? super E> getRightPredicate() {
            return right;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return left.op(element) || right.op(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") or (" + right + ")";
        }
    }

    /**
     * A Predicate that always evaluates to <tt>true</tt>. Use {@link #TRUE} to get an instance of this Predicate.
     * 
     * @see FalsePredicate
     */
    public static final class TruePredicate implements Predicate<Object>, BytePredicate, CharPredicate,
            DoublePredicate, FloatPredicate, IntPredicate, LongPredicate, ShortPredicate, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** Creates a new TruePredicate. */
        TruePredicate() {
        }

        /**
         * Returns <tt>true</tt> for any element.
         * 
         * @param element
         *            the element to test
         * @return <tt>true</tt> for any element
         */
        public boolean op(Object element) {
            return true;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TRUE;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return Boolean.TRUE.toString();
        }

        public boolean op(byte a) {
            return true;
        }

        public boolean op(char a) {
            return true;
        }

        public boolean op(double a) {
            return true;
        }

        public boolean op(float a) {
            return true;
        }

        public boolean op(int a) {
            return true;
        }

        public boolean op(long a) {
            return true;
        }

        public boolean op(short a) {
            return true;
        }
    }

    /**
     * A Predicate that performs a logical exclusive OR (XOR) on two supplied predicates.
     */
    public static final class XorPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The left side operand. */
        private final Predicate<? super E> left;

        /** The right side operand. */
        private final Predicate<? super E> right;

        /**
         * Creates a new <code>XorPredicate</code>.
         * 
         * @param left
         *            the left side Predicate
         * @param right
         *            the right side Predicate
         * @throws NullPointerException
         *             if any of the supplied predicates are <code>null</code>
         */
        public XorPredicate(Predicate<? super E> left, Predicate<? super E> right) {
            if (left == null) {
                throw new NullPointerException("left is null");
            } else if (right == null) {
                throw new NullPointerException("right is null");
            }
            this.left = left;
            this.right = right;
        }

        /**
         * Returns the left side Predicate.
         * 
         * @return the left side Predicate.
         */
        public Predicate<? super E> getLeftPredicate() {
            return left;
        }

        /**
         * Returns the right side Predicate.
         * 
         * @return the right side Predicate.
         */
        public Predicate<? super E> getRightPredicate() {
            return right;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return left.op(element) ^ right.op(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "(" + left + ") xor (" + right + ")";
        }
    }

    /**
     * Creates an array of predicates from an {@link Iterable}.
     * 
     * @param iterable
     *            the iterable to convert
     * @return and array of predicate
     * @param <E>
     *            the type of the predicates
     */
    static <E> Predicate<E>[] iterableToArray(Iterable<? extends Predicate<? super E>> iterable) {
        if (iterable == null) {
            throw new NullPointerException("iterable is null");
        }
        ArrayList<Predicate<?>> list = new ArrayList<Predicate<?>>();
        for (Predicate<?> p : iterable) {
            if (p == null) {
                throw new NullPointerException("iterable contained a null");
            }
            list.add(p);
        }
        return (Predicate[]) list.toArray(new Predicate[list.size()]);
    }

}
