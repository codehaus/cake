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
package org.codehaus.cake.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.codehaus.cake.collection.Maps;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;

/**
 * Various ops implementations that operate on {@link Collection}s, {@link Iterable}s and {@link Map}s.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id: CollectionOps.java 285 2009-02-24 18:40:44Z kasper $
 */
public final class Iterables {

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private Iterables() {
    }

    // /CLOVER:ON

    /**
     * Wraps the {@link Collection#add(Object)} method in an {@link Procedure}.
     * <p>
     * The returned Procedure is serializable if the specified Collection is serializable.
     * 
     * @param collection
     *            the collection to wrap
     * @return the newly created Procedure
     * @throws NullPointerException
     *             if the specified collection is <code>null</code>
     * @param <E>
     *            the types of elements accepted by the specified Collection
     */
    public static <E> Procedure<E> addToCollectionProcedure(Collection<? super E> collection) {
        return new CollectionAdd<E>(collection);
    }

    /**
     * Creates a {@link Predicate} that will accept only those elements that are in the specified collection.
     * 
     * @param collection
     *            the collection that is tested against
     * @return the newly created Predicate
     */
    public static Predicate containedWithin(Collection collection) {
        return new ContainsPredicate(collection);
    }

    /**
     * Filters the specified iterable, returning a new list of those items that evaluated to <code>true</code> given
     * the specified predicate.
     * 
     * @param <E>
     *            the types of items that are filtered
     * @param iterable
     *            the iterable to filter
     * @param predicate
     *            the predicate to evaluate items accordingly to
     * @return a new list of filteres items
     */
    public static <E> List<E> filteredList(Iterable<E> iterable, Predicate<? super E> predicate) {
        if (iterable == null) {
            throw new NullPointerException("iterable is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        List<E> list = new ArrayList<E>();
        for (E e : iterable) {
            if (predicate.op(e)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns <code>true</code> if <b>all</b> of elements in the specified iterable can be accepted by the specified
     * predicate, otherwise <code>false</code>.
     * 
     * @param <E>
     *            the types of elements
     * @param iterable
     *            the iterable to check
     * @param predicate
     *            the predicate to test against
     * @return true if all elements are accepted, otherwise false
     */
    public static <E> boolean isAllTrue(Iterable<E> iterable, Predicate<? super E> predicate) {
        if (iterable == null) {
            throw new NullPointerException("iterable is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        for (E s : iterable) {
            if (!predicate.op(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether or not <b>any</b> of elements in the specified can be accepted by the specified predicate.
     * 
     * @param <E>
     *            the types accepted
     * @param iterable
     *            the iterable to check
     * @param predicate
     *            the predicate to test against
     * @return whether or not any of elements in the specified can be accepted by the specified predicate
     */
    public static <E> boolean isAnyTrue(Iterable<E> iterable, Predicate<? super E> predicate) {
        if (iterable == null) {
            throw new NullPointerException("iterable is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        for (E s : iterable) {
            if (predicate.op(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Wraps the {@link Queue#offer(Object)} method in an {@link Procedure}.
     * <p>
     * The returned Procedure is serializable if the specified Queue is serializable.
     * 
     * @param queue
     *            the queue to wrap
     * @return the newly created Procedure
     * @throws NullPointerException
     *             if the specified queue is <code>null</code>
     * @param <E>
     *            the types of elements accepted by the specified Queue
     */
    public static <E> Procedure<E> offerToQueue(Queue<? super E> queue) {
        return new QueueOffer<E>(queue);
    }

    public static <E> void retain(Iterable<E> iterable, Predicate<? super E> predicate) {
        if (iterable == null) {
            throw new NullPointerException("iterable is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        for (Iterator<E> i = iterable.iterator(); i.hasNext();) {
            if (!predicate.op(i.next())) {
                i.remove();
            }
        }
    }

    /** Wraps the {@link Collection#add(Object)} method in an {@link Procedure}. */
    static final class CollectionAdd<E> implements Procedure<E>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The collection we are wrapping. */
        private final Collection<? super E> collection;

        /**
         * Creates a new CollectionAdd.
         * 
         * @param collection
         *            the Collection to wrap
         * @throws NullPointerException
         *             if the specified collection is <code>null</code>
         */
        public CollectionAdd(Collection<? super E> collection) {
            if (collection == null) {
                throw new NullPointerException("collection is null");
            }
            this.collection = collection;
        }

        /** {@inheritDoc} */
        public void op(E element) {
            collection.add(element); // ignore return value
        }
    }

    /**
     * A Predicate that evaluates to <code>true</code> iff the element being evaluated is contained in the collection
     * specified when construction it.
     */
    static final class ContainsPredicate<E> implements Predicate<E>, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** The element to compare with. */
        private final Collection<E> collection;

        /**
         * Creates a ContainsPredicate.
         * 
         * @param collection
         *            the collection of elements
         * @throws NullPointerException
         *             if the specified collection is <code>null</code>
         */
        public ContainsPredicate(Collection<E> collection) {
            if (collection == null) {
                throw new NullPointerException("collection is null");
            }
            this.collection = collection;
        }

        /** {@inheritDoc} */
        public boolean op(E element) {
            return collection.contains(element);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "isContained In " + collection;
        }
    }

    /** Wraps the {@link Queue#offer(Object)} method in an {@link Procedure}. */
    static final class QueueOffer<E> implements Procedure<E>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The queue we are wrapping. */
        private final Queue<? super E> queue;

        /**
         * Creates a new QueueOffer.
         * 
         * @param queue
         *            the Queue to wrap
         * @throws NullPointerException
         *             if the specified queue is <code>null</code>
         */
        public QueueOffer(final Queue<? super E> queue) {
            if (queue == null) {
                throw new NullPointerException("queue is null");
            }
            this.queue = queue;
        }

        /** {@inheritDoc} */
        public void op(E element) {
            queue.offer(element); // ignore return value
        }
    }

}
