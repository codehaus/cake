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
package org.codehaus.cake.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.util.ops.Ops.ByteComparator;
import org.codehaus.cake.util.ops.Ops.CharComparator;
import org.codehaus.cake.util.ops.Ops.DoubleComparator;
import org.codehaus.cake.util.ops.Ops.FloatComparator;
import org.codehaus.cake.util.ops.Ops.IntComparator;
import org.codehaus.cake.util.ops.Ops.LongComparator;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.ShortComparator;

/**
 * Various implementations of {@link Comparator}, {@link FloatComparator}, {@link LongComparator},
 * {@link DoubleComparator} and {@link IntComparator}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class Comparators {

    /** A Comparator for Comparable.objects using their <i>natural ordering</i>. The comparator is Serializable. */
    public static final Comparator NATURAL_COMPARATOR = new NaturalComparator();

    /** A comparator that imposes the reverse of the <i>natural ordering</i>. This comparator is Serializable. */
    public static final Comparator NATURAL_REVERSE_COMPARATOR = new NaturalReverseComparator();

    public static final Comparator NULL_GREATEST_ORDER = new NullGreatestOrderPredicate();

    /** A Comparator for Comparable.objects. The comparator is Serializable. */
    public static final Comparator NULL_LEAST_ORDER = new NullLeastOrderPredicate();

    /** A comparator for doubles relying on natural ordering. The comparator is Serializable. */
    public static final DoubleComparator DOUBLE_NATURAL_COMPARATOR = (DoubleComparator) Comparators.NATURAL_COMPARATOR;

    /** A comparator that imposes the reverse of the <i>natural ordering</i> on doubles. The comparator is Serializable. */
    public static final DoubleComparator DOUBLE_NATURAL_REVERSE_COMPARATOR = (DoubleComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

    /** A comparator for floats relying on natural ordering. The comparator is Serializable. */
    public static final FloatComparator FLOAT_NATURAL_COMPARATOR = (FloatComparator) Comparators.NATURAL_COMPARATOR;

    /** A comparator that imposes the reverse of the <i>natural ordering</i> on floats. The comparator is Serializable. */
    public static final FloatComparator FLOAT_NATURAL_REVERSE_COMPARATOR = (FloatComparator) Comparators.NATURAL_REVERSE_COMPARATOR;
    
    /** A comparator for ints relying on natural ordering. The comparator is Serializable. */
    public static final IntComparator INT_NATURAL_COMPARATOR = (IntComparator) Comparators.NATURAL_COMPARATOR;

    /** A comparator that imposes the reverse of the <i>natural ordering</i> on ints. The comparator is Serializable. */
    public static final IntComparator INT_NATURAL_REVERSE_COMPARATOR = (IntComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

    /** A comparator for longs relying on natural ordering. The comparator is Serializable. */
    public static final LongComparator LONG_NATURAL_COMPARATOR = (LongComparator) Comparators.NATURAL_COMPARATOR;

    /** A comparator that imposes the reverse of the <i>natural ordering</i> on longs. The comparator is Serializable. */
    public static final LongComparator LONG_NATURAL_REVERSE_COMPARATOR = (LongComparator) Comparators.NATURAL_REVERSE_COMPARATOR;

    /** Cannot instantiate. */
    private Comparators() {
    }

    public static <T> Comparator<T> compoundComparator(Comparator<? super T> first, Comparator<? super T> second) {
        if (first == null) {
            throw new NullPointerException("first is null");
        } else if (second == null) {
            throw new NullPointerException("second is null");
        }
        return new CompoundComparator<T>(first, second);
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> mappedComparator(Op<? super T, U> mapper) {
        return mappedComparator(mapper, NATURAL_COMPARATOR);
    }

    public static <T, U> Comparator<T> mappedComparator(Op<? super T, U> mapper, Comparator<? super U> comparator) {
        return new MappedComparator<T, U>(comparator, mapper);
    }

    /**
     * Returns a Comparator that use the objects natural comparator. The returned comparator is serializable.
     * <p>
     * This example illustrates the type-safe way to obtain a natural comparator:
     * 
     * <pre>
     * Comparator&lt;String&gt; s = Comparators.naturalComparator();
     * </pre>
     * 
     * Implementation note: Implementations of this method need not create a separate <tt>comparator</tt> object for
     * each call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this
     * method, the field does not provide type safety.)
     * 
     * @return a comparator for Comparable.objects
     * @param <T>
     *            the type of elements accepted by the comparator
     */
    public static <T extends Comparable> Comparator<T> naturalComparator() {
        return NATURAL_COMPARATOR;
    }

    public static <T extends Comparable> Comparator<T> nullGreatestOrder() {
        return NULL_GREATEST_ORDER;
    }

    public static <T extends Comparable> Comparator<T> nullGreatestOrder(Comparator<T> comparator) {
        return new NullGreatestOrderComparatorPredicate<T>(comparator);
    }

    public static <T extends Comparable> Comparator<T> nullLeastOrder() {
        return NULL_LEAST_ORDER;
    }

    public static <T extends Comparable> Comparator<T> nullLeastOrder(Comparator<T> comparator) {
        return new NullLeastOrderComparatorPredicate<T>(comparator);
    }


    // public static <T> Comparator<T> mappedComparator(ObjectToLong<? super T> mapper) {
    // throw new UnsupportedOperationException();
    // }
    //
    // public static <T> Comparator<T> mappedComparator(ObjectToLong<? super T> mapper,
    // LongComparator comparator) {
    // throw new UnsupportedOperationException();
    // }

    /**
     * Returns a comparator that imposes the reverse of the <i>natural ordering</i> on a collection of objects that
     * implement the <tt>Comparable</tt> interface. (The natural ordering is the ordering imposed by the objects' own
     * <tt>compareTo</tt> method.) This enables a simple idiom for sorting (or maintaining) collections (or arrays) of
     * objects that implement the <tt>Comparable</tt> interface in reverse-natural-order.
     * <p>
     * The returned comparator is serializable.
     * 
     * @return a comparator that imposes the reverse of the <i>natural ordering</i> on a collection of objects that
     *         implement the <tt>Comparable</tt> interface.
     * @param <T>
     *            the Comparable types accepted by the Comparator
     * @see Comparable
     */
    public static <T extends Comparable> Comparator<T> reverseOrder() {
        return NATURAL_REVERSE_COMPARATOR;
    }

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also serializable).
     * 
     * @param comparator
     *            the Comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     * @param <T>
     *            the Comparable types accepted by the Comparator
     */
    public static <T> Comparator<T> reverseOrder(Comparator<T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return Collections.reverseOrder(comparator);
    }

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also serializable).
     * 
     * @param comparator
     *            the comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     */
    public static DoubleComparator reverseOrder(DoubleComparator comparator) {
        return new ReverseDoubleComparator(comparator);
    }

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also serializable).
     * 
     * @param comparator
     *            the comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     */
    public static FloatComparator reverseOrder(FloatComparator comparator) {
        return new ReverseFloatComparator(comparator);
    }

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also serializable).
     * 
     * @param comparator
     *            the comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     */
    public static IntComparator reverseOrder(IntComparator comparator) {
        return new ReverseIntComparator(comparator);
    }

    /**
     * Creates a comparator that imposes the reverse ordering of the specified comparator.
     * <p>
     * The returned comparator is serializable (assuming the specified comparator is also serializable).
     * 
     * @param comparator
     *            the comparator to reverse
     * @return a comparator that imposes the reverse ordering of the specified comparator.
     */
    public static LongComparator reverseOrder(LongComparator comparator) {
        return new ReverseLongComparator(comparator);
    }

    public static <T> Comparator<? super T> stack(List<? extends Comparator<? super T>> comparators) {
        if (comparators == null) {
            throw new NullPointerException("comparators is null");
        } else if (comparators.size() == 0) {
            throw new IllegalArgumentException("no comparators in list");
        }
        for (Object c : comparators) {
            if (c == null) {
                throw new NullPointerException("list contains a null comparator");
            }
        }
        Comparator<? super T> first = comparators.get(0);
        if (comparators.size() == 1) {
            return first;
        }
        Comparator<? super T> second = comparators.get(1);
        if (comparators.size() == 2) {
            return new CompoundComparator<T>(first, second);
        }

        Comparator<? super T>[] list = new Comparator[comparators.size() - 2];
        for (int i = 2; i < comparators.size(); i++) {
            list[i - 2] = comparators.get(i);
        }
        return new StackedComparator<T>(first, second, list);
    }

    static class CompoundComparator<T> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final Comparator<? super T> first;
        private final Comparator<? super T> second;

        public CompoundComparator(Comparator<? super T> first, Comparator<? super T> second) {
            this.first = first;
            this.second = second;
        }

        public int compare(T o1, T o2) {
            int result = first.compare(o1, o2);
            if (result == 0) {
                return second.compare(o1, o2);
            }
            return result;
        }
    }

    /** A Comparator for Comparable.objects. */
    static final class MappedComparator<T, U> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final Comparator<? super U> comparator;

        private final Op<? super T, U> mapper;

        MappedComparator(Comparator<? super U> comparator, Op<? super T, U> mapper) {
            if (mapper == null) {
                throw new NullPointerException("mapper is null");
            } else if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.mapper = mapper;
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            U ua = mapper.op(a);
            U ub = mapper.op(b);
            return comparator.compare(ua, ub);
        }
    }

    /** A Comparator for Comparable.objects. */
    static final class NaturalComparator<T extends Comparable<? super T>> implements ByteComparator, CharComparator,
            DoubleComparator, FloatComparator, IntComparator, LongComparator, ShortComparator, Comparator<T>,
            Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public int compare(byte a, byte b) {
            return a - b;
        }

        public int compare(char a, char b) {
            return a - b;
        }

        public int compare(double a, double b) {
            return Double.compare(a, b);
        }

        public int compare(float a, float b) {
            return Float.compare(a, b);
        }

        public int compare(int a, int b) {
            return a < b ? -1 : a > b ? 1 : 0;
        }

        public int compare(long a, long b) {
            return a < b ? -1 : a > b ? 1 : 0;
        }

        public int compare(short a, short b) {
            return a - b;
        }

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return a.compareTo(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NATURAL_COMPARATOR;
        }
    }

    /** A Comparator for Comparable.objects. */
    static final class NaturalReverseComparator<T extends Comparable<? super T>> implements ByteComparator,
            CharComparator, DoubleComparator, FloatComparator, IntComparator, LongComparator, ShortComparator,
            Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public int compare(byte a, byte b) {
            return b - a;
        }

        public int compare(char a, char b) {
            return b - a;
        }

        public int compare(double a, double b) {
            return Double.compare(b, a);
        }

        public int compare(float a, float b) {
            return Float.compare(b, a);
        }

        public int compare(int a, int b) {
            return a < b ? 1 : a > b ? -1 : 0;
        }

        public int compare(long a, long b) {
            return a < b ? 1 : a > b ? -1 : 0;
        }

        public int compare(short a, short b) {
            return b - a;
        }

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return b.compareTo(a);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NATURAL_REVERSE_COMPARATOR;
        }
    }

    static final class NullGreatestOrderComparatorPredicate<T> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -5918068122775742745L;

        private final Comparator<T> comparator;

        NullGreatestOrderComparatorPredicate(Comparator<T> comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return a == null ? b == null ? 0 : 1 : b == null ? -1 : comparator.compare(a, b);
        }
    }

    static final class NullGreatestOrderPredicate<T extends Comparable<? super T>> implements Comparator<T>,
            Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 4313874045537757310L;

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return a == null ? b == null ? 0 : 1 : b == null ? -1 : a.compareTo(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NULL_GREATEST_ORDER;
        }
    }

    static final class NullLeastOrderComparatorPredicate<T> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -5918068122775742745L;

        private final Comparator<T> comparator;

        NullLeastOrderComparatorPredicate(Comparator<T> comparator) {
            if (comparator == null) {
                throw new NullPointerException("comparator is null");
            }
            this.comparator = comparator;
        }

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return a == null ? b == null ? 0 : -1 : b == null ? 1 : comparator.compare(a, b);
        }
    }

    static final class NullLeastOrderPredicate<T extends Comparable<? super T>> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -5791305305191186665L;

        /** {@inheritDoc} */
        public int compare(T a, T b) {
            return a == null ? b == null ? 0 : -1 : b == null ? 1 : a.compareTo(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NULL_LEAST_ORDER;
        }
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

    static class StackedComparator<T> implements Comparator<T>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        private final Comparator<? super T>[] comparators;
        private final Comparator<? super T> first;
        private final Comparator<? super T> second;

        public StackedComparator(Comparator<? super T> first, Comparator<? super T> second,
                Comparator<? super T>[] comparators) {
            this.first = first;
            this.second = second;
            this.comparators = comparators;
        }

        public int compare(T o1, T o2) {
            int result = first.compare(o1, o2);
            if (result == 0) {
                result = second.compare(o1, o2);
                if (result == 0) {
                    for (int i = 0; i < comparators.length; i++) {
                        result = comparators[i].compare(o1, o2);
                        if (result != 0) {
                            return result;
                        }
                    }
                }
            }
            return result;
        }
    }
}
