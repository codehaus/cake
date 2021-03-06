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
package org.codehaus.cake.cache.policy;

import java.util.Map;

import org.codehaus.cake.cache.policy.PolicyContext.ObjectAttachment;

/**
 * An abstract class that can be used to implements a replacement policy that relies on a double linked list of
 * references. For example, a least recently used (LRU) policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractDoubleLinkedReplacementPolicy.java 225 2008-11-30 20:53:08Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractDoubleLinkedReplacementPolicy<T> extends AbstractReplacementPolicy<T> {
    private T first;

    private T last;
    private final ObjectAttachment<T> next;
    private final ObjectAttachment<T> prev;

    public AbstractDoubleLinkedReplacementPolicy(PolicyContext<T> context) {
        next = context.attachSelfReference();
        prev = context.attachSelfReference();
    }

    /**
     * Adds the specified entry to the front of the list.
     * 
     * @param entry
     *            the entry to add to the front of the list
     */
    protected final void addFirst(T entry) {
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
    protected final void addLast(T entry) {
        if (first == null) {
            first = entry;
        } else {
            setNext(last, entry);
            setPrev(entry, last);
        }
        last = entry;
    }

    /** Clears the linked list. */
    public void clear() {
        first = null;
        last = null;
    }

    /** @return the first node in the list, or <code>null</code> if it is empty. */
    protected final T getFirst() {
        return first;
    }

    /** @return the last entry of the list, or <code>null</code> if it is empty. */
    protected final T getLast() {
        return last;
    }

    /**
     * Returns the node after the specified entry, or <code>null</code> if it is the last node.
     * 
     * @param entry
     *            the node to return the next node for
     * @return the next node for the specified node, or <code>null</code> if it is the last node
     */
    protected final T getNext(T entry) {
        return next.get(entry);
    }

    /**
     * Returns the node before the specified entry, or <code>null</code> if it is the first node.
     * 
     * @param entry
     *            the node to return the previous node for
     * @return the previous node for the specified node, or <code>null</code> if it is the first node
     */
    protected final T getPrevious(T entry) {
        return prev.get(entry);
    }

    /**
     * Moves the specified entry to the head of the queue.
     * 
     * @param entry
     *            the entry to move to the head of the list
     */
    protected final void moveFirst(T entry) {
        T prev = getPrevious(entry);
        T next = getNext(entry);
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
    protected final void moveLast(T entry) {
        T prev = getPrevious(entry);
        T next = getNext(entry);

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

//    /**
//     * Used for debugging.
//     */
//    public void print() {
//        T e = first;
//        int count = 0;
//        System.out.println("first: " + getKey(first));
//        while (e != null) {
//            System.out.println(count++ + " " + getKey(getPrevious(e)) + "<-" + getKey(e) + "<-"
//                    + getKey(getNext(e)));
//            e = getNext(e);
//        }
//        System.out.println("last: " + getKey(last));
//        System.out.println("-------------------");
//
//    }
//
//    private Object getKey(Object o) {
//        return o instanceof Map.Entry ? ((Map.Entry) o).getKey() : o;
//    }

    /** Removes the specified entry from the linked list. */
    public void remove(T t) {
        T prev = getPrevious(t);
        T next = getNext(t);
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
     * Removes the first entry of the linked list.
     * 
     * @return the first entry of the linked list or <code>null</code> if the list is empty
     */
    protected final T removeFirst() {
        T entry = first;
        if (first != null) {
            remove(first);
        }
        return entry;
    }

    /**
     * Removes the last element of the linked list.
     * 
     * @return the last element of the linked list or <code>null</code> if the list is empty
     */
    protected final T removeLast() {
        T entry = last;
        if (last != null) {
            remove(last);
        }
        return entry;
    }

    /**
     * Removes the specified previous entry from the linked list. Next add is called with the specified newEntry,
     * Finally, the specified newEntry is returned.
     */
    public void replace(T previous, T newEntry) {
        T prev = getPrevious(previous);
        T next = getNext(previous);
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
     * Sets the next entry for the specified entry.
     * 
     * @param entry
     *            the entry to set the pointer for
     * @param next
     *            the next entry to point to
     */
    private void setNext(T entry, T next) {
        this.next.set(entry, next);
    }

    /**
     * Sets the previous entry for the specified entry.
     * 
     * @param entry
     *            the entry to set the pointer for
     * @param next
     *            the previous entry to point to
     */
    private void setPrev(T entry, T next) {
        this.prev.set(entry, next);
    }
}
