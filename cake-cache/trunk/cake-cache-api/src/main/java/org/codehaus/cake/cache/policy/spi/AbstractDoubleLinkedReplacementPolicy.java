package org.codehaus.cake.cache.policy.spi;

import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.attribute.CacheAttributeService;

/**
 * An abstract class that can be used to implements a replacement policy that relies on a double
 * linked list of references. For example, a least recently used (LRU) policy.
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
public abstract class AbstractDoubleLinkedReplacementPolicy<K, V> extends
        AbstractReplacementPolicy<K, V> {
    private CacheEntry<K, V> head;
    private final ObjectAttribute<CacheEntry<K, V>> nextPointer = new ObjectAttribute("next",
            CacheEntry.class) {};
    private final ObjectAttribute<CacheEntry<K, V>> prevPointer = new ObjectAttribute("prev",
            CacheEntry.class) {};

    private CacheEntry<K, V> tail;

    /**
     * Adds the specified entry to the front of the list.
     * 
     * @param entry
     *            the entry to add to the front of the list
     */
    protected void addHead(CacheEntry<K, V> entry) {
        if (tail == null) {
            tail = entry;
        } else {
            setPrev(head, entry);
            setNext(entry, head);
            nextPointer.set(entry, head);
        }
        head = entry;
    }

    /**
     * Adds the specified entry to the end of the list.
     * 
     * @param entry
     *            the entry to add to the end of the list
     */
    protected void addTail(CacheEntry<K, V> entry) {
        if (head == null) {
            head = entry;
        } else {
            nextPointer.set(tail, entry);
            prevPointer.set(entry, tail);
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
    protected CacheEntry<K, V> getHead() {
        return head;
    }

    private String getKey(CacheEntry<K, V> entry) {
        return entry == null ? null : entry.getKey() + "";
    }

    /**
     * @return the tail of the list.
     */
    protected CacheEntry<K, V> getTail() {
        return tail;
    }

    /**
     * Moves the specified entry to the head of the queue.
     * 
     * @param entry
     *            the entry to move to the head of the list
     */
    protected void moveToHead(CacheEntry<K, V> entry) {
        CacheEntry<K, V> prev = prevPointer.get(entry);
        CacheEntry<K, V> next = nextPointer.get(entry);
        if (prev != null) {// check if already first
            if (next == null) {
                tail = prev;
            } else {
                prevPointer.set(next, prev);
            }

            prevPointer.set(entry, null);// help gc
            nextPointer.set(entry, head);
            prevPointer.set(head, entry);
            nextPointer.set(prev, next);
        }
        head = entry;
    }

    /**
     * Moves the specified entry to the tail of the queue.
     * 
     * @param entry
     *            the entry to move to the tail of the list
     */
    protected void moveToTail(CacheEntry<K, V> t) {
        CacheEntry<K, V> prev = prevPointer.get(t);
        CacheEntry<K, V> next = nextPointer.get(t);

        if (next != null) {// check if already last
            if (prev == null) {
                head = next;
            } else {
                nextPointer.set(prev, next);
            }
            nextPointer.set(t, null);// help gc
            prevPointer.set(t, tail);
            prevPointer.set(next, prev);
            nextPointer.set(tail, t);
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

    public void verify() {
        boolean fail = false;
        if (head == null) {
            fail = tail != null;
        }
    }

    protected void realReplace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        CacheEntry<K, V> prev = (CacheEntry<K, V>) prevPointer.get(previous);
        CacheEntry<K, V> next = (CacheEntry<K, V>) nextPointer.get(previous);
        prevPointer.set(newEntry, prev);
        nextPointer.set(newEntry, next);
        prevPointer.set(previous, null);
        nextPointer.set(previous, null);
        // TODO update head or tail????
    }

    /**
     * Removes the specified entry from the linked list.
     */
    public void remove(CacheEntry<K, V> t) {
        CacheEntry<K, V> prev = (CacheEntry<K, V>) prevPointer.get(t);
        CacheEntry<K, V> next = (CacheEntry<K, V>) nextPointer.get(t);
        if (prev == null) {
            head = next;
        } else {
            nextPointer.set(prev, next);
        }
        if (next == null) {
            tail = prev;
        } else {
            prevPointer.set(next, prev);
        }
    }

    /**
     * Removes the head of the linked list.
     * 
     * @return the head of the linked list or <code>null</code> if the list is empty
     */
    protected CacheEntry<K, V> removeHead() {
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
    protected CacheEntry<K, V> removeTail() {
        CacheEntry<K, V> entry = tail;
        if (tail != null) {
            remove(tail);
        }
        return entry;
    }

    /**
     * Removes the specified previous entry from the linked list. Next add is called with the
     * specified newEntry, Finally, the specified newEntry is returned.
     */
    @Override
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

    /**
     * Registers the necessary hooks into the cache for this replacement policy.
     * 
     * @param service
     *            the CacheAttributeService
     */
    public void start(CacheAttributeService service) {
        service.registerAttribute(prevPointer);
        service.registerAttribute(nextPointer);
    }
}
