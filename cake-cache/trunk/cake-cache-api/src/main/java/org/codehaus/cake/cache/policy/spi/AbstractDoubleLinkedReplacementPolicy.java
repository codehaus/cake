package org.codehaus.cake.cache.policy.spi;

import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheEntry;

/**
 * An abstract class that can be used to implements a replacement policy that relies on a double linked list of
 * references. For example, a least recently used (LRU) policy.
 * <p>
 * See
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractDoubleLinkedReplacementPolicy<K, V> extends AbstractReplacementPolicy<K, V> {
    private CacheEntry<K, V> head;

    private final ObjectAttribute<CacheEntry> nextPointer = new ObjectAttribute<CacheEntry>("next", CacheEntry.class) {};

    private final ObjectAttribute<CacheEntry> prevPointer = new ObjectAttribute<CacheEntry>("prev", CacheEntry.class) {};

    private CacheEntry<K, V> tail;

    public AbstractDoubleLinkedReplacementPolicy() {
        attachToEntry(prevPointer);
        attachToEntry(nextPointer);
    }

    /**
     * Adds the specified entry to the front of the list.
     * 
     * @param entry
     *            the entry to add to the front of the list
     */
    protected final void addHead(CacheEntry<K, V> entry) {
        if (tail == null) {
            tail = entry;
        } else {
            setPrev(head, entry);
            setNext(entry, head);
        }
        head = entry;
    }

    /**
     * Adds the specified entry to the end of the list.
     * 
     * @param entry
     *            the entry to add to the end of the list
     */
    protected final void addTail(CacheEntry<K, V> entry) {
        if (head == null) {
            head = entry;
        } else {
            setNext(tail, entry);
            setPrev(entry, tail);
        }
        tail = entry;
    }

    /**
     * Clears the linked list.
     */
    public void clear() {
        head = null;
        tail = null;
    }

    /**
     * @return the head of the list.
     */
    protected final CacheEntry<K, V> getHead() {
        return head;
    }

    private String getKey(CacheEntry<K, V> entry) {
        return entry == null ? null : entry.getKey() + "";
    }

    protected final CacheEntry<K, V> getNext(CacheEntry<K, V> entry) {
        return nextPointer.get(entry);
    }

    protected final CacheEntry<K, V> getPrevious(CacheEntry<K, V> entry) {
        return prevPointer.get(entry);
    }

    /**
     * @return the tail of the list.
     */
    protected final CacheEntry<K, V> getTail() {
        return tail;
    }

    /**
     * Moves the specified entry to the head of the queue.
     * 
     * @param entry
     *            the entry to move to the head of the list
     */
    protected final void moveToHead(CacheEntry<K, V> entry) {
        CacheEntry<K, V> prev = getPrevious(entry);
        CacheEntry<K, V> next = getNext(entry);
        if (prev != null) {// check if already first
            if (next == null) {
                tail = prev;
            } else {
                setPrev(next, prev);
            }

            setPrev(entry, null);// help gc
            setNext(entry, head);
            setPrev(head, entry);
            setNext(prev, next);
        }
        head = entry;
    }

    /**
     * Moves the specified entry to the tail of the queue.
     * 
     * @param entry
     *            the entry to move to the tail of the list
     */
    protected final void moveToTail(CacheEntry<K, V> t) {
        CacheEntry<K, V> prev = getPrevious(t);
        CacheEntry<K, V> next = getNext(t);

        if (next != null) {// check if already last
            if (prev == null) {
                head = next;
            } else {
                setNext(prev, next);
            }
            setNext(t, null);// help gc
            setPrev(t, tail);
            setPrev(next, prev);
            setNext(tail, t);
        }
        tail = t;
    }

    /**
     * Used for debugging.
     */
    public void print() {
        CacheEntry<K, V> e = head;
        int count = 0;
        System.out.println("first: " + getKey(head));
        while (e != null) {
            System.out.println(count++ + " " + getKey(prevPointer.get(e)) + "<-" + getKey(e) + "<-"
                    + getKey(nextPointer.get(e)));
            e = nextPointer.get(e);
        }
        System.out.println("last: " + getKey(tail));
        System.out.println("-------------------");

    }

    protected void realReplace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        CacheEntry<K, V> prev = getPrevious(previous);
        CacheEntry<K, V> next = getNext(previous);
        setPrev(newEntry, prev);
        setNext(newEntry, next);
        setPrev(previous, null);
        setNext(previous, null);
        // TODO update head or tail????
    }

    /**
     * Removes the specified entry from the linked list.
     */
    public void remove(CacheEntry<K, V> t) {
        CacheEntry<K, V> prev = getPrevious(t);
        CacheEntry<K, V> next = getNext(t);
        if (prev == null) {
            head = next;
        } else {
            setNext(prev, next);
        }
        if (next == null) {
            tail = prev;
        } else {
            setPrev(next, prev);
        }
    }

    /**
     * Removes the head of the linked list.
     * 
     * @return the head of the linked list or <code>null</code> if the list is empty
     */
    protected final CacheEntry<K, V> removeHead() {
        CacheEntry<K, V> entry = head;
        if (head != null) {
            remove(head);
        }
        return entry;
    }

    /**
     * Removes the tail of the linked list.
     * 
     * @return the tail of the linked list or <code>null</code> if the list is empty
     */
    protected final CacheEntry<K, V> removeTail() {
        CacheEntry<K, V> entry = tail;
        if (tail != null) {
            remove(tail);
        }
        return entry;
    }

    /**
     * Removes the specified previous entry from the linked list. Next add is called with the specified newEntry,
     * Finally, the specified newEntry is returned.
     */
    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        remove(previous);
        add(newEntry);
        return newEntry;
    }

    private void setNext(CacheEntry<K, V> entry, CacheEntry<K, V> next) {
        entry.getAttributes().put(nextPointer, next);
    }

    private void setPrev(CacheEntry<K, V> entry, CacheEntry<K, V> next) {
        entry.getAttributes().put(prevPointer, next);
    }
}
