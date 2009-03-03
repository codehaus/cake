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

import org.codehaus.cake.cache.policy.PolicyContext.IntAttachment;

/**
 * An abstract implementation of a replacement policy that uses a list for ordering entries.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractArrayReplacementPolicy.java 225 2008-11-30 20:53:08Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractArrayReplacementPolicy<T> extends AbstractCakeReplacementPolicy<T> {
    /**
     * The array buffer into which the elements of this policy is stored. The capacity of the ArrayList is the length of
     * this array buffer.
     */
    private T[] elementData;

    private final IntAttachment idx;

    /** The size of the ArrayList (the number of elements it contains). */
    private int size;

    /**
     * Creates a new AbstractArrayReplacementPolicy.
     * 
     * @param context
     *            the context needed for constructing the policy
     */
    public AbstractArrayReplacementPolicy(PolicyContext<T> context) {
        idx = context.attachInt(-1);
        elementData = context.newArray(16);
    }

    /** {@inheritDoc} */
    public void add(T entry) {
        addLast(entry);
    }

    /**
     * Appends the specified element to the end of this array.
     * 
     * @param entry
     *            the entry to add
     * @return the index of the added entry
     */
    protected int addLast(T entry) {
        int i = size;
        ensureCapacity(size + 1);
        setFromIndex(entry, i);
        elementData[size++] = entry;
        return i;
    }

    /** {@inheritDoc} */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            Object oldData[] = elementData;
            int newCapacity = (oldCapacity * 3) / 2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            elementData = (T[]) new Object[newCapacity];
            System.arraycopy(oldData, 0, elementData, 0, size);
        }
    }

    /**
     * Return the element for the specified index
     * 
     * @param index
     *            the index of the entry
     * @throws IndexOutOfBoundsException
     *             if index is out of range <tt>(index
     *        &lt; 0 || index &gt;= size())</tt>.
     * @return the element at the specified position in this array
     */
    protected final T getFromIndex(int index) {
        rangeCheck(index);
        return elementData[index];
    }

    /**
     * Returns the index of the specified entry, or -1 if the entry has not been registered
     * 
     * @param entry
     *            the entry to return the index for
     * @return the index of the specified entry, or -1 if the entry has not been registered
     */
    protected final int getIndexOf(T entry) {
        return idx.get(entry);
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /** {@inheritDoc} */
    public void remove(T entry) {
        removeEntry(entry);
    }

    protected T removeByIndex(int index) {
        T entry = getFromIndex(index);
        int lastIndex = --size;
        T last = elementData[lastIndex];
        elementData[lastIndex] = null;
        if (entry != last) {
            elementData[index] = last;
            setFromIndex(last, index);
            removed(entry, lastIndex, last, index);
        }
        return entry;
    }

    protected void removed(T entry, int prevIndex, T replacedWith, int newIndex) {
    }

    protected int removeEntry(T entry) {
        int lastIndex = --size;
        T last = elementData[lastIndex];
        elementData[lastIndex] = null;
        if (entry != last) {
            int i = getIndexOf(entry);
            elementData[i] = last;
            setFromIndex(last, i);
            removed(entry, lastIndex, last, i);
        }
        return lastIndex;
    }

    /** {@inheritDoc} */
    public void replace(T previous, T newEntry) {
        int i = getIndexOf(previous);
        setFromIndex(newEntry, i);
        elementData[i] = newEntry;
    }

    protected final void setFromIndex(T entry, int in) {
        idx.set(entry, in);
    }

    /** @return the number of entries in the replacement policy */
    public final int size() {
        return size;
    }
}
