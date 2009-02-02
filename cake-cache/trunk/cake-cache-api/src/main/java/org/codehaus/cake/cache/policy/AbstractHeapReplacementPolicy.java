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
package org.codehaus.cake.cache.policy;

import java.util.Comparator;

import org.codehaus.cake.cache.policy.spi.PolicyContext;
import org.codehaus.cake.cache.policy.spi.PolicyContext.IntAttachment;
import org.codehaus.cake.internal.util.ArrayUtils;

// TODO shouldn't implement Comparator, much better just to add a method that
// should be overriden
public abstract class AbstractHeapReplacementPolicy<T> extends AbstractCakeReplacementPolicy<T> implements
        Comparator<T> {

    private final Comparator<? super T> comparator = this;

    /** The attribute that is used to keep track of the index into heap of a given CacheEntry. */
    private final IntAttachment idx;

    /**
     * Priority queue represented as a balanced binary heap: the two children of queue[n] are queue[2*n+1] and
     * queue[2*(n+1)]. The priority queue is ordered by comparator For each node n in the heap and each descendant d of
     * n, n <= d. The element with the lowest value is in queue[0], assuming the queue is nonempty.
     */
    T[] queue;

    /** The number of elements in the priority queue. */
    private int size;

    public AbstractHeapReplacementPolicy(PolicyContext<T> context) {
        queue = context.newArray(0);
        idx = context.attachInt();
    }

    /** {@inheritDoc} */
    public void add(T entry) {
        int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0) {
            queue[0] = entry;
            setIndexOf(entry, 0);
        } else {
            siftUp(i, entry);
        }
    }

    /** {@inheritDoc} */
    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    public final int compare(T o1, T o2) {
        return compareEntry(o1, o2);
    }

    protected abstract int compareEntry(T o1, T o2);

    /** {@inheritDoc} */
    public T evictNext() {
        if (size == 0)
            return null;
        int s = --size;
        T result = (T) queue[0];
        T x = (T) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        return result;
    }

    /**
     * Increases the capacity of the array.
     * 
     * @param minCapacity
     *            the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // / CLOVER:OFF
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        // / CLOVER:ON
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64) ? ((oldCapacity + 1) * 2) : ((oldCapacity / 2) * 3));
        // / CLOVER:OFF
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        // / CLOVER:ON
        queue = ArrayUtils.copyOf(queue, newCapacity);
    }

    private int indexOf(T entry) {
        return idx.get(entry);
    }

    /**
     * @return the element that will be evicted the next time, or <code>null</code> if the heap is empty
     */
    protected T peek() {
        if (size == 0)
            return null;
        return (T) queue[0];
    }

    /** {@inheritDoc} */
    public void remove(T entry) {
        removeAt(indexOf(entry));
    }

    /**
     * Removes the ith element from queue.
     * 
     * Normally this method leaves the elements at up to i-1, inclusive, untouched. Under these circumstances, it
     * returns null. Occasionally, in order to maintain the heap invariant, it must swap a later element of the list
     * with one earlier than i. Under these circumstances, this method returns the element that was previously at the
     * end of the list and is now at some position before i. This fact is used by iterator.remove so as to avoid missing
     * traversing elements.
     */
    private T removeAt(int i) {
        // assert i >= 0 && i < size;
        int s = --size;
        if (s == i) // removed last element
            queue[i] = null;
        else {
            T moved = queue[s];
            queue[s] = null;
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
                if (queue[i] != moved)
                    return moved;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    public void replace(T previous, T newEntry) {
        int i = comparator.compare(previous, newEntry);
        int index = indexOf(previous);
        setIndexOf(newEntry, index);
        queue[index] = newEntry;
        if (i > 0) {
            siftUp(index, newEntry);
        } else if (i < 0) {
            siftDown(index, newEntry);
        }
    }

    private void setIndexOf(T entry, int index) {
        idx.set(entry, index);
    }

    protected void siftDown(T x) {
        siftDown(indexOf(x), x);
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by demoting x down the tree repeatedly until it is less
     * than or equal to its children or is a leaf.
     * 
     * @param k
     *            the position to fill
     * @param x
     *            the item to insert
     */
    private void siftDown(int k, T x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = queue[child];
            int right = child + 1;
            if (right < size && comparator.compare((T) c, (T) queue[right]) > 0) {
                c = queue[child = right];
            }
            if (comparator.compare(x, (T) c) <= 0) {
                break;
            }
            queue[k] = c;
            setIndexOf(c, k);
            k = child;
        }
        queue[k] = x;
        setIndexOf(x, k);
    }

    protected void siftUp(T x) {
        siftUp(indexOf(x), x);
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by promoting x up the tree until it is greater than or
     * equal to its parent, or is the root.
     * 
     * To simplify and speed up coercions and comparisons. the Comparable and Comparator versions are separated into
     * different methods that are otherwise identical. (Similarly for siftDown.)
     * 
     * @param k
     *            the position to fill
     * @param x
     *            the item to insert
     */
    private void siftUp(int k, T x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            T e = queue[parent];
            if (comparator.compare(x, (T) e) >= 0)
                break;
            queue[k] = e;
            setIndexOf(e, k);
            k = parent;
        }
        queue[k] = x;
        setIndexOf(x, k);
    }
}
