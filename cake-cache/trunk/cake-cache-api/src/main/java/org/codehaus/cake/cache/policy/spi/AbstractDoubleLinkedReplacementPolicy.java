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
    private CacheEntry<K, V> first;

    private final ObjectAttribute<CacheEntry> nextPointer = new ObjectAttribute<CacheEntry>("next", CacheEntry.class) {};

    private final ObjectAttribute<CacheEntry> prevPointer = new ObjectAttribute<CacheEntry>("prev", CacheEntry.class) {};

    private CacheEntry<K, V> last;

    public AbstractDoubleLinkedReplacementPolicy() {
        attachToEntry(prevPointer);
        attachToEntry(nextPointer);
    }
//
//    public int size() {
//        int count = 0;
//        CacheEntry<K, V> e = first;
//        while (e != null) {
//            count++;
//            e = getNext(e);
//        }
//        return count;
//
//    }

    /**
     * Adds the specified entry to the front of the list.
     * 
     * @param entry
     *            the entry to add to the front of the list
     */
    protected final void addFirst(CacheEntry<K, V> entry) {
        if (last == null) {
            last = entry;
        } else {
            setPrev(first, entry);
            setNext(entry, first);
        }
        first = entry;
    }

    /**
     * Adds the specified entry to the end of the list.
     * 
     * @param entry
     *            the entry to add to the end of the list
     */
    protected final void addLast(CacheEntry<K, V> entry) {
        if (first == null) {
            first = entry;
        } else {
            setNext(last, entry);
            setPrev(entry, last);
        }
        last = entry;
    }

    /**
     * Clears the linked list.
     */
    public void clear() {
        first = null;
        last = null;
    }

    /**
     * @return the head of the list.
     */
    protected final CacheEntry<K, V> getFirst() {
        return first;
    }

    //
    // private String getKey(CacheEntry<K, V> entry) {
    // return entry == null ? null : entry.getKey() + "";
    // }

    protected final CacheEntry<K, V> getNext(CacheEntry<K, V> entry) {
        return nextPointer.get(entry);
    }

    protected final CacheEntry<K, V> getPrevious(CacheEntry<K, V> entry) {
        return prevPointer.get(entry);
    }

    /**
     * @return the tail of the list.
     */
    protected final CacheEntry<K, V> getLast() {
        return last;
    }

    /**
     * Moves the specified entry to the head of the queue.
     * 
     * @param entry
     *            the entry to move to the head of the list
     */
    protected final void moveFirst(CacheEntry<K, V> entry) {
        CacheEntry<K, V> prev = getPrevious(entry);
        CacheEntry<K, V> next = getNext(entry);
        if (prev != null) {// check if already first
            if (next == null) {
                last = prev;
            } else {
                setPrev(next, prev);
            }

            setPrev(entry, null);// help gc
            setNext(entry, first);
            setPrev(first, entry);
            setNext(prev, next);
        }
        first = entry;
    }

    /**
     * Moves the specified entry to the tail of the queue.
     * 
     * @param entry
     *            the entry to move to the tail of the list
     */
    protected final void moveLast(CacheEntry<K, V> entry) {
        CacheEntry<K, V> prev = getPrevious(entry);
        CacheEntry<K, V> next = getNext(entry);

        if (next != null) {// check if already last
            if (prev == null) {
                first = next;
            } else {
                setNext(prev, next);
            }
            setNext(entry, null);// help gc
            setPrev(entry, last);
            setPrev(next, prev);
            setNext(last, entry);
        }
        last = entry;
    }

    // /**
    // * Used for debugging.
    // */
    // public void print() {
    // CacheEntry<K, V> e = first;
    // int count = 0;
    // System.out.println("first: " + getKey(first));
    // while (e != null) {
    // System.out.println(count++ + " " + getKey(prevPointer.get(e)) + "<-" + getKey(e) + "<-"
    // + getKey(nextPointer.get(e)));
    // e = nextPointer.get(e);
    // }
    // System.out.println("last: " + getKey(last));
    // System.out.println("-------------------");
    //
    // }

    /**
     * Removes the specified entry from the linked list.
     */
    public void remove(CacheEntry<K, V> t) {
        CacheEntry<K, V> prev = getPrevious(t);
        CacheEntry<K, V> next = getNext(t);
        if (prev == null) {
            first = next;
        } else {
            setNext(prev, next);
        }
        if (next == null) {
            last = prev;
        } else {
            setPrev(next, prev);
        }
    }

    /**
     * Removes the head of the linked list.
     * 
     * @return the head of the linked list or <code>null</code> if the list is empty
     */
    protected final CacheEntry<K, V> removeFirst() {
        CacheEntry<K, V> entry = first;
        if (first != null) {
            remove(first);
        }
        return entry;
    }

    /**
     * Removes the tail of the linked list.
     * 
     * @return the tail of the linked list or <code>null</code> if the list is empty
     */
    protected final CacheEntry<K, V> removeLast() {
        CacheEntry<K, V> entry = last;
        if (last != null) {
            remove(last);
        }
        return entry;
    }

    protected void replace0(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        CacheEntry<K, V> prev = getPrevious(previous);
        CacheEntry<K, V> next = getNext(previous);
        setPrev(newEntry, prev);
        setNext(newEntry, next);

        if (prev == null) {
            first = newEntry;
        } else {
            setNext(prev, newEntry);
        }
        if (next == null) {
            last = newEntry;
        } else {
            setPrev(next, newEntry);
        }
    }

    /**
     * Removes the specified previous entry from the linked list. Next add is called with the specified newEntry,
     * Finally, the specified newEntry is returned.
     */
    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        replace0(previous, newEntry);
        return newEntry;
    }

    private void setNext(CacheEntry<K, V> entry, CacheEntry<K, V> next) {
        entry.getAttributes().put(nextPointer, next);
    }

    private void setPrev(CacheEntry<K, V> entry, CacheEntry<K, V> next) {
        entry.getAttributes().put(prevPointer, next);
    }
}
