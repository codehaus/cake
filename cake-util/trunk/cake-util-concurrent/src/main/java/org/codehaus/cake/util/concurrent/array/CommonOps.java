/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package org.codehaus.cake.util.concurrent.array;
import java.util.Comparator;

import org.codehaus.cake.util.concurrent.ForkJoinWorkerThread;
import org.codehaus.cake.util.ops.Ops.BinaryDoublePredicate;
import org.codehaus.cake.util.ops.Ops.BinaryIntPredicate;
import org.codehaus.cake.util.ops.Ops.BinaryLongPredicate;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.DoubleComparator;
import org.codehaus.cake.util.ops.Ops.DoubleGenerator;
import org.codehaus.cake.util.ops.Ops.DoubleOp;
import org.codehaus.cake.util.ops.Ops.DoubleReducer;
import org.codehaus.cake.util.ops.Ops.DoubleToLong;
import org.codehaus.cake.util.ops.Ops.DoubleToObject;
import org.codehaus.cake.util.ops.Ops.IntGenerator;
import org.codehaus.cake.util.ops.Ops.LongComparator;
import org.codehaus.cake.util.ops.Ops.LongGenerator;
import org.codehaus.cake.util.ops.Ops.LongOp;
import org.codehaus.cake.util.ops.Ops.LongReducer;
import org.codehaus.cake.util.ops.Ops.LongToDouble;
import org.codehaus.cake.util.ops.Ops.LongToObject;
import org.codehaus.cake.util.ops.Ops.ObjectToDouble;
import org.codehaus.cake.util.ops.Ops.ObjectToLong;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.Reducer;

/**
 * A collection of static factory methods providing commonly useful
 * implementations of operations.
 */
class CommonOps {
    private CommonOps() {} // disable construction

    /**
     * Returns a Comparator for Comparable objects
     */
    public static <T extends Comparable<? super T>> Comparator<T>
                             naturalComparator(Class<T> type) {
        return new Comparator<T>() {
            public int compare(T a, T b) { return a.compareTo(b); }
        };
    }

    /**
     * Returns a reducer returning the maximum of two Comparable
     * elements, treating null as less than any non-null element.
     */
    public static <T extends Comparable<? super T>> Reducer<T>
                             naturalMaxReducer(Class<T> type) {
        return new Reducer<T>() {
            public T op(T a, T b) {
                return (a != null &&
                        (b == null || a.compareTo(b) >= 0))? a : b;
            }
        };
    }

    /**
     * Returns a reducer returning the minimum of two Comparable
     * elements, treating null as greater than any non-null element.
     */
    public static <T extends Comparable<? super T>> Reducer<T>
                             naturalMinReducer(Class<T> type) {
        return new Reducer<T>() {
            public T op(T a, T b) {
                return (a != null &&
                        (b == null || a.compareTo(b) <= 0))? a : b;
            }
        };
    }

    /**
     * Returns a reducer returning the maximum of two elements, using
     * the given comparator, and treating null as less than any
     * non-null element.
     */
    public static <T> Reducer<T> maxReducer
        (final Comparator<? super T> comparator) {
        return new Reducer<T>() {
            public T op(T a, T b) {
                return (a != null &&
                        (b == null || comparator.compare(a, b) >= 0))? a : b;
            }
        };
    }

    /**
     * Returns a reducer returning the minimum of two elements, using
     * the given comparator, and treating null as greater than any
     * non-null element.
     */
    public static <T> Reducer<T> minReducer
        (final Comparator<? super T> comparator) {
        return new Reducer<T>() {
            public T op(T a, T b) {
                return (a != null &&
                        (b == null || comparator.compare(a, b) <= 0))? a : b;
            }
        };
    }

    /**
     * Returns a Comparator that casts its arguments as Comparable on
     * each comparison, throwing ClassCastException on failure.
     */
    public static Comparator<Object> castedComparator() {
        return (Comparator<Object>)(RawComparator.cmp);
    }
    static final class RawComparator implements Comparator {
        static final RawComparator cmp = new RawComparator();
        public int compare(Object a, Object b) {
            return ((Comparable)a).compareTo((Comparable)b);
        }
    }

    /**
     * Returns a reducer returning maximum of two values, or
     * <tt>null</tt> if both arguments are null, and that casts
     * its arguments as Comparable on each comparison, throwing
     * ClassCastException on failure.
     */
    public static Reducer<Object> castedMaxReducer() {
        return (Reducer<Object>)RawMaxReducer.max;
    }
    static final class RawMaxReducer implements Reducer {
        static final RawMaxReducer max = new RawMaxReducer();
        public Object op(Object a, Object b) {
            return (a != null &&
                    (b == null ||
                     ((Comparable)a).compareTo((Comparable)b) >= 0))? a : b;
        }
    }

    /**
     * Returns a reducer returning minimum of two values, or
     * <tt>null</tt> if both arguments are null, and that casts
     * its arguments as Comparable on each comparison, throwing
     * ClassCastException on failure.
     */
    public static Reducer<Object> castedMinReducer() {
        return (Reducer<Object>)RawMinReducer.min;
    }
    static final class RawMinReducer implements Reducer {
        static final RawMinReducer min = new RawMinReducer();
        public Object op(Object a, Object b) {
            return (a != null &&
                    (b == null ||
                     ((Comparable)a).compareTo((Comparable)b) <= 0))? a : b;
        }
    }


    /**
     * Returns a comparator for doubles relying on natural ordering
     */
    public static DoubleComparator naturalDoubleComparator() {
        return NaturalDoubleComparator.comparator;
    }
    static final class NaturalDoubleComparator
        implements DoubleComparator {
        static final NaturalDoubleComparator comparator = new
            NaturalDoubleComparator();
        public int compare(double a, double b) {
            return Double.compare(a, b);
        }
    }


    static final class NaturalDoubleMaxReducer
        implements DoubleReducer {
        public static final NaturalDoubleMaxReducer max =
            new NaturalDoubleMaxReducer();
        public double op(double a, double b) { return Math.max(a, b); }
    }

    /**
     * Returns a reducer returning the maximum of two double elements,
     * using the given comparator
     */
    public static DoubleReducer doubleMaxReducer
        (final DoubleComparator comparator) {
        return new DoubleReducer() {
                public double op(double a, double b) {
                    return (comparator.compare(a, b) >= 0)? a : b;
                }
            };
    }

    /**
     * Returns a reducer returning the minimum of two double elements,
     * using the given comparator
     */
    public static DoubleReducer doubleMinReducer
        (final DoubleComparator comparator) {
        return new DoubleReducer() {
                public double op(double a, double b) {
                    return (comparator.compare(a, b) <= 0)? a : b;
                }
            };
    }

    /**
     * Returns a comparator for longs relying on natural ordering
     */
    public static LongComparator naturalLongComparator() {
        return NaturalLongComparator.comparator;
    }
    static final class NaturalLongComparator
        implements LongComparator {
        static final NaturalLongComparator comparator = new
            NaturalLongComparator();
        public int compare(long a, long b) {
            return a < b? -1 : ((a > b)? 1 : 0);
        }
    }




    /**
     * Returns a reducer returning the maximum of two long elements,
     * using the given comparator
     */
    public static LongReducer longMaxReducer
        (final LongComparator comparator) {
        return new LongReducer() {
                public long op(long a, long b) {
                    return (comparator.compare(a, b) >= 0)? a : b;
                }
            };
    }

    /**
     * Returns a reducer returning the minimum of two long elements,
     * using the given comparator
     */
    public static LongReducer longMinReducer
        (final LongComparator comparator) {
        return new LongReducer() {
                public long op(long a, long b) {
                    return (comparator.compare(a, b) <= 0)? a : b;
                }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,U,V> Op<T,V> compoundOp
        (final Op<? super T, ? extends U> first,
         final Op<? super U, ? extends V> second) {
        return new Op<T,V>() {
            public final V op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,V> Op<T,V> compoundOp
        (final ObjectToDouble<? super T> first,
         final DoubleToObject<? extends V> second) {
        return new Op<T,V>() {
            public final V op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,V> Op<T,V> compoundOp
        (final ObjectToLong<? super T> first,
         final LongToObject<? extends V> second) {
        return new Op<T,V>() {
            public final V op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,V> DoubleToObject<V> compoundOp
        (final DoubleToObject<? extends T> first,
         final Op<? super T,? extends V> second) {
        return new DoubleToObject<V>() {
            public final V op(double t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,V> LongToObject<V> compoundOp
        (final LongToObject<? extends T> first,
         final Op<? super T,? extends V> second) {
        return new LongToObject<V>() {
            public final V op(long t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,U> ObjectToDouble<T> compoundOp
        (final Op<? super T, ? extends U> first,
         final ObjectToDouble<? super U> second) {
        return new ObjectToDouble<T>() {
            public final double op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T,U> ObjectToLong<T> compoundOp
        (final Op<? super T, ? extends U> first,
         final ObjectToLong<? super U> second) {
        return new ObjectToLong<T>() {
            public final long op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> ObjectToDouble<T> compoundOp
        (final ObjectToDouble<? super T> first,
         final DoubleOp second) {
        return new ObjectToDouble<T>() {
            public final double op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> ObjectToLong<T> compoundOp
        (final ObjectToDouble<? super T> first,
         final DoubleToLong second) {
        return new ObjectToLong<T>() {
            public final long op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> ObjectToLong<T> compoundOp
        (final ObjectToLong<? super T> first,
         final LongOp second) {
        return new ObjectToLong<T>() {
            public final long op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> ObjectToDouble<T> compoundOp
        (final ObjectToLong<? super T> first,
         final LongToDouble second) {
        return new ObjectToDouble<T>() {
            public final double op(T t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static DoubleOp compoundOp
        (final DoubleOp first,
         final DoubleOp second) {
        return new DoubleOp() {
                public final double op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static DoubleToLong compoundOp
        (final DoubleOp first,
         final DoubleToLong second) {
        return new DoubleToLong() {
                public final long op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static DoubleToLong compoundOp
        (final DoubleToLong first,
         final LongOp second) {
        return new DoubleToLong() {
                public final long op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> DoubleToObject<T> compoundOp
        (final DoubleToLong first,
         final LongToObject<? extends T> second) {
        return new DoubleToObject<T>() {
            public final T op(double t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> LongToObject<T> compoundOp
        (final LongToDouble first,
         final DoubleToObject<? extends T> second) {
        return new LongToObject<T>() {
            public final T op(long t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static LongToDouble compoundOp
        (final LongOp first,
         final LongToDouble second) {
        return new LongToDouble() {
                public final double op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static LongToDouble compoundOp
        (final LongToDouble first,
         final DoubleOp second) {
        return new LongToDouble() {
                public final double op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> DoubleToObject<T> compoundOp
        (final DoubleOp first,
         final DoubleToObject<? extends T> second) {
        return new DoubleToObject<T>() {
            public final T op(double t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> LongToObject<T> compoundOp
        (final LongOp first,
         final LongToObject<? extends T> second) {
        return new LongToObject<T>() {
            public final T op(long t) { return second.op(first.op(t)); }
        };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> DoubleOp compoundOp
        (final DoubleToObject<? extends T> first,
         final ObjectToDouble<? super T>  second) {
        return new DoubleOp() {
                public final double op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> LongToDouble compoundOp
        (final LongToObject<? extends T> first,
         final ObjectToDouble<? super T>  second) {
        return new LongToDouble() {
                public final double op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> DoubleToLong compoundOp
        (final DoubleToObject<? extends T> first,
         final ObjectToLong<? super T>  second) {
        return new DoubleToLong() {
                public final long op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static <T> LongOp compoundOp
        (final LongToObject<? extends T> first,
         final ObjectToLong<? super T>  second) {
        return new LongOp() {
                public final long op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static LongOp compoundOp
        (final LongOp first,
         final LongOp second) {
        return new LongOp() {
                public final long op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static DoubleOp compoundOp
        (final DoubleToLong first,
         final LongToDouble second) {
        return new DoubleOp() {
                public final double op(double t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a composite mapper that applies a second mapper to the results
     * of applying the first one
     */
    public static LongOp compoundOp
        (final LongToDouble first,
         final DoubleToLong second) {
        return new LongOp() {
                public final long op(long t) { return second.op(first.op(t)); }
            };
    }

    /**
     * Returns a predicate evaluating to the conjunction of its contained predicates
     */
    public static <S, T extends S> Predicate<T> andPredicate
                                (final Predicate<S> first,
                                 final Predicate<? super T> second) {
        return new Predicate<T>() {
            public final boolean op(T x) {
                return first.op(x) && second.op(x);
            }
        };
    }

    /**
     * Returns a predicate evaluating to the disjunction of its contained predicates
     */
    public static <S, T extends S> Predicate<T> orPredicate
                                (final Predicate<S> first,
                                 final Predicate<? super T> second) {
        return new Predicate<T>() {
            public final boolean op(T x) {
                return first.op(x) || second.op(x);
            }
        };
    }

    /**
     * Returns a predicate evaluating to true if its argument is non-null
     */
    public static  Predicate<Object> isNonNullPredicate() {
        return IsNonNullPredicate.predicate;
    }
    static final class IsNonNullPredicate implements Predicate<Object> {
        static final IsNonNullPredicate predicate =
            new IsNonNullPredicate();
        public final boolean op(Object x) {
            return x != null;
        }
    }

    /**
     * Returns a predicate evaluating to true if its argument is an instance
     * of (see {@link Class#isInstance} the given type (class).
     */
    public static Predicate<Object> instanceofPredicate(final Class type) {
        return new Predicate<Object>() {
            public final boolean op(Object x) {
                return type.isInstance(x);
            }
        };
    }

    /**
     * Returns a predicate evaluating to true if its argument is assignable
     * from (see {@link Class#isAssignableFrom} the given type (class).
     */
    public static Predicate<Object> isAssignablePredicate(final Class type) {
        return new Predicate<Object>() {
            public final boolean op(Object x) {
                return type.isAssignableFrom(x.getClass());
            }
        };
    }


    /**
     * Returns a generator producing uniform random values between
     * zero and one, with the same properties as {@link
     * java.util.Random#nextDouble} but operating independently across
     * ForkJoinWorkerThreads and usable only within forkjoin
     * computations.
     */
    public static DoubleGenerator doubleRandom() {
        return DoubleRandomGenerator.generator;
    }
    static final class DoubleRandomGenerator implements DoubleGenerator {
        static final DoubleRandomGenerator generator =
            new DoubleRandomGenerator();
        public double op() {
            return ForkJoinWorkerThread.nextRandomDouble();
        }
    }

    /**
     * Returns a generator producing uniform random values between
     * zero and the given bound, with the same properties as {@link
     * java.util.Random#nextDouble} but operating independently across
     * ForkJoinWorkerThreads and usable only within forkjoin
     * computations.
     * @param bound the upper bound (exclusive) of opd values
     */
    public static DoubleGenerator doubleRandom(double bound) {
        return new DoubleBoundedRandomGenerator(bound);
    }
    static final class DoubleBoundedRandomGenerator implements DoubleGenerator {
        final double bound;
        DoubleBoundedRandomGenerator(double bound) { this.bound = bound; }
        public double op() {
            return ForkJoinWorkerThread.nextRandomDouble() * bound;
        }
    }

    /**
     * Returns a generator producing uniform random values between the
     * given least value (inclusive) and bound (exclusive), operating
     * independently across ForkJoinWorkerThreads and usable only
     * within forkjoin computations.
     * @param least the least value returned
     * @param bound the upper bound (exclusive) of opd values
     */
    public static DoubleGenerator doubleRandom(double least, double bound) {
        return new DoubleIntervalRandomGenerator(least, bound);
    }
    static final class DoubleIntervalRandomGenerator implements DoubleGenerator {
        final double least;
        final double range;
        DoubleIntervalRandomGenerator(double least, double bound) {
            this.least = least; this.range = bound - least;
        }
        public double op() {
            return ForkJoinWorkerThread.nextRandomDouble() * range + least;
        }
    }

    /**
     * Returns a generator producing uniform random values with the
     * same properties as {@link java.util.Random#nextLong} but
     * operating independently across ForkJoinWorkerThreads and usable
     * only within forkjoin computations.
     */
    public static LongGenerator longRandom() {
        return LongRandomGenerator.generator;
    }
    static final class LongRandomGenerator implements LongGenerator {
        static final LongRandomGenerator generator =
            new LongRandomGenerator();
        public long op() {
            return ForkJoinWorkerThread.nextRandomLong();
        }
    }

    /**
     * Returns a generator producing uniform random values with the
     * same properties as {@link java.util.Random#nextInt(int)} but
     * operating independently across ForkJoinWorkerThreads and usable
     * only within forkjoin computations.
     * @param bound the upper bound (exclusive) of opd values
     */
    public static LongGenerator longRandom(long bound) {
        if (bound <= 0)
            throw new IllegalArgumentException();
        return new LongBoundedRandomGenerator(bound);
    }
    static final class LongBoundedRandomGenerator implements LongGenerator {
        final long bound;
        LongBoundedRandomGenerator(long bound) { this.bound = bound; }
        public long op() {
            return ForkJoinWorkerThread.nextRandomLong(bound);
        }
    }

    /**
     * Returns a generator producing uniform random values between the
     * given least value (inclusive) and bound (exclusive), operating
     * independently across ForkJoinWorkerThreads and usable only
     * within forkjoin computations.
     * @param least the least value returned
     * @param bound the upper bound (exclusive) of opd values
     */
    public static LongGenerator longRandom(long least, long bound) {
        if (least >= bound)
            throw new IllegalArgumentException();
        return new LongIntervalRandomGenerator(least, bound);
    }
    static final class LongIntervalRandomGenerator implements LongGenerator {
        final long least;
        final long range;
        LongIntervalRandomGenerator(long least, long bound) {
            this.least = least; this.range = bound - least;
        }
        public long op() {
            return ForkJoinWorkerThread.nextRandomLong(range) + least;
        }
    }

    /**
     * Returns a generator producing uniform random values with the
     * same properties as {@link java.util.Random#nextInt} but
     * operating independently across ForkJoinWorkerThreads and usable
     * only within forkjoin computations.
     */
    public static IntGenerator intRandom() {
        return IntRandomGenerator.generator;
    }
    static final class IntRandomGenerator implements IntGenerator {
        static final IntRandomGenerator generator =
            new IntRandomGenerator();
        public int op() {
            return ForkJoinWorkerThread.nextRandomInt();
        }
    }

    /**
     * Returns a generator producing uniform random values with the
     * same properties as {@link java.util.Random#nextInt(int)} but
     * operating independently across ForkJoinWorkerThreads and usable
     * only within forkjoin computations.
     * @param bound the upper bound (exclusive) of opd values
     */
    public static IntGenerator intRandom(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException();
        return new IntBoundedRandomGenerator(bound);
    }
    static final class IntBoundedRandomGenerator implements IntGenerator {
        final int bound;
        IntBoundedRandomGenerator(int bound) { this.bound = bound; }
        public int op() {
            return ForkJoinWorkerThread.nextRandomInt(bound);
        }
    }

    /**
     * Returns a generator producing uniform random values between the
     * given least value (inclusive) and bound (exclusive), operating
     * independently across ForkJoinWorkerThreads and usable only
     * within forkjoin computations.
     * @param least the least value returned
     * @param bound the upper bound (exclusive) of opd values
     */
    public static IntGenerator intRandom(int least, int bound) {
        if (least >= bound)
            throw new IllegalArgumentException();
        return new IntIntervalRandomGenerator(least, bound);
    }
    static final class IntIntervalRandomGenerator implements IntGenerator {
        final int least;
        final int range;
        IntIntervalRandomGenerator(int least, int bound) {
            this.least = least; this.range = bound - least;
        }
        public int op() {
            return ForkJoinWorkerThread.nextRandomInt(range) + least;
        }
    }

    /**
     * Returns a predicate evaluating to true if the
     * first argument <tt>!equals</tt> the second
     */
    public static BinaryPredicate<Object, Object> inequalityPredicate() {
        return InequalityPredicate.predicate;
    }
    static final class InequalityPredicate implements BinaryPredicate<Object, Object> {
        static final InequalityPredicate predicate =
            new InequalityPredicate();
        public final boolean op(Object x, Object y) {
            return !x.equals(y);
        }
    }

    /**
     * Returns a predicate evaluating to true if the
     * first argument <tt>!=</tt> the second
     */
    public static BinaryPredicate<Object, Object> nonidentityPredicate() {
        return NonidentityPredicate.predicate;
    }
    static final class NonidentityPredicate implements BinaryPredicate<Object, Object> {
        static final NonidentityPredicate predicate =
            new NonidentityPredicate();
        public final boolean op(Object x, Object y) {
            return x != y;
        }
    }

    /**
     * Returns a predicate evaluating to true if the
     * first argument <tt>!=</tt> the second
     */
    public static BinaryIntPredicate intInequalityPredicate() {
        return IntInequalityPredicate.predicate;
    }
    static final class IntInequalityPredicate implements BinaryIntPredicate {
        static final IntInequalityPredicate predicate =
            new IntInequalityPredicate();
        public final boolean op(int x, int y) {
            return x != y;
        }
    }

    /**
     * Returns a predicate evaluating to true if the
     * first argument <tt>==</tt> the second
     */
    public static BinaryLongPredicate longInequalityPredicate() {
        return LongInequalityPredicate.predicate;
    }
    static final class LongInequalityPredicate implements BinaryLongPredicate {
        static final LongInequalityPredicate predicate =
            new LongInequalityPredicate();
        public final boolean op(long x, long y) {
            return x != y;
        }
    }

    /**
     * Returns a predicate evaluating to true if the
     * first argument <tt>!=</tt> the second
     */
    public static BinaryDoublePredicate doubleInequalityPredicate() {
        return DoubleInequalityPredicate.predicate;
    }
    static final class DoubleInequalityPredicate implements BinaryDoublePredicate {
        static final DoubleInequalityPredicate predicate =
            new DoubleInequalityPredicate();
        public final boolean op(double x, double y) {
            return x != y;
        }
    }


}
