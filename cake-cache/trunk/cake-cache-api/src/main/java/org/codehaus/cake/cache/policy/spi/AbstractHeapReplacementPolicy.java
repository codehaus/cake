package org.codehaus.cake.cache.policy.spi;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import java.util.Comparator;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.util.ArrayUtils;

//TODO shouldn't implement Comparator, much better just to add a method that should be overriden
public abstract class AbstractHeapReplacementPolicy<K, V> extends AbstractReplacementPolicy<K, V> implements
        Comparator<CacheEntry<K, V>> {

    /**
     * Priority queue represented as a balanced binary heap: the two children of queue[n] are queue[2*n+1] and
     * queue[2*(n+1)]. The priority queue is ordered by comparator For each node n in the heap and each descendant d of
     * n, n <= d. The element with the lowest value is in queue[0], assuming the queue is nonempty.
     */
    private CacheEntry[] queue = new CacheEntry[0];
    private final Comparator<? super CacheEntry<K, V>> comparator = this;
    /**
     * The number of elements in the priority queue.
     */
    private int size = 0;

    private final IntAttribute index = new IntAttribute("index", 0) {};

    public AbstractHeapReplacementPolicy() {
        attachToEntry(index);
    }

    private int indexOf(CacheEntry<K, V> entry) {
        return index.get(entry);
    }

    private void setIndexOf(CacheEntry<K, V> entry, int index) {
        entry.getAttributes().put(this.index, index);
    }

    public boolean add(CacheEntry<K, V> entry) {
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
        return true;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            queue[i] = null;
        }
        size = 0;
    }

    public CacheEntry<K, V> evictNext() {
        if (size == 0)
            return null;
        int s = --size;
        CacheEntry<K, V> result = (CacheEntry<K, V>) queue[0];
        CacheEntry<K, V> x = (CacheEntry<K, V>) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        return result;
    }

    public void remove(CacheEntry<K, V> entry) {
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
    private CacheEntry<K, V> removeAt(int i) {
        assert i >= 0 && i < size;
        int s = --size;
        if (s == i) // removed last element
            queue[i] = null;
        else {
            CacheEntry<K, V> moved = queue[s];
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

    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        int i = comparator.compare(previous, newEntry);
        int index = indexOf(previous);
        setIndexOf(newEntry, index);
        queue[index] = newEntry;
        if (i > 0) {
            siftUp(index, newEntry);
        } else if (0 < i) {
            siftDown(index, newEntry);
        }
        return newEntry;
    }

    protected abstract int compareEntry(CacheEntry<K, V> o1, CacheEntry<K, V> o2);

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
    private void siftUp(int k, CacheEntry<K, V> x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            CacheEntry<K, V> e = queue[parent];
            if (comparator.compare(x, (CacheEntry<K, V>) e) >= 0)
                break;
            queue[k] = e;
            setIndexOf(e, k);
            k = parent;
        }
        queue[k] = x;
        setIndexOf(x, k);
    }

    protected void siftDown(CacheEntry<K, V> x) {
        siftDown(indexOf(x), x);
    }

    protected void siftUp(CacheEntry<K, V> x) {
        siftUp(indexOf(x), x);
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
    private void siftDown(int k, CacheEntry<K, V> x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            CacheEntry<K, V> c = queue[child];
            int right = child + 1;
            if (right < size && comparator.compare((CacheEntry<K, V>) c, (CacheEntry<K, V>) queue[right]) > 0)
                c = queue[child = right];
            if (comparator.compare(x, (CacheEntry<K, V>) c) <= 0)
                break;
            queue[k] = c;
            setIndexOf(c, k);
            k = child;
        }
        queue[k] = x;
        setIndexOf(x, k);
    }

    /**
     * Increases the capacity of the array.
     * 
     * @param minCapacity
     *            the desired minimum capacity
     */
    private void grow(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = ((oldCapacity < 64) ? ((oldCapacity + 1) * 2) : ((oldCapacity / 2) * 3));
        if (newCapacity < 0) // overflow
            newCapacity = Integer.MAX_VALUE;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        queue = ArrayUtils.copyOf(queue, newCapacity);
    }

    public final int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return compareEntry(o1, o2);
    }
}
