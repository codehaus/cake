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

import java.util.ArrayList;

import org.codehaus.cake.cache.CacheEntry;

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractArrayReplacementPolicy.java 225 2008-11-30 20:53:08Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractArrayReplacementPolicy<K, V> extends AbstractCakeReplacementPolicy<K, V> {
    private final ArrayList<CacheEntry<K, V>> list = new ArrayList<CacheEntry<K, V>>();

    private Reg<?> idx;

    /** {@inheritDoc} */
    public boolean add(CacheEntry<K, V> entry) {
        add0(entry);
        return true;
    }

    protected int add0(CacheEntry<K, V> entry) {
        int i = list.size();
        setFromIndex(entry, i);
        list.add(entry);
        return i;
    }

    /** {@inheritDoc} */
    public void clear() {
        list.clear();
    }

    /**
     * @param index
     *            the index of the attribute
     * @return
     */
    protected CacheEntry<K, V> getFromIndex(int index) {
        return list.get(index);
    }


    /**
     * Returns the index of the specified entry, or -1 if the entry has not been registered
     * 
     * @param entry
     *            the entry to return the index for
     * @return the index of the specified entry, or -1 if the entry has not been registered
     */
    protected int getIndexOf(CacheEntry<K, V> entry) {
        return idx.getInt(entry);
    }
    protected void setFromIndex(CacheEntry<K, V> entry, int in) {
        idx.setInt(entry, in);
    }
    /** {@inheritDoc} */
    public void remove(CacheEntry<K, V> entry) {
        remove0(entry);
    }

    protected CacheEntry<K, V> removeByIndex(int index) {
        CacheEntry<K, V> entry = list.get(index);
        remove0(entry);
        return entry;
    }

    protected int remove0(CacheEntry<K, V> entry) {
        int lastIndex = list.size() - 1;
        CacheEntry<K, V> last = list.remove(lastIndex);
        if (entry != last) {
            int i = getIndexOf(entry);
            list.set(i, last);
            setFromIndex(last, i);
            swap(lastIndex, i);
        }
        return lastIndex;
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        replace0(previous, newEntry);
        return newEntry;
    }

    protected void replace0(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        int i = getIndexOf(previous);
        setFromIndex(newEntry, i);
        list.set(i, newEntry);
    }

    /**
     * @return the number of entries in the replacement policy
     */
    protected int size() {
        return list.size();
    }

    protected void swap(int prevIndex, int newIndex) {
    }

    @Override
    protected <T> void register(
            org.codehaus.cake.cache.policy.AbstractCakeReplacementPolicy.ReadWriterGenerator generator) {
        idx = generator.newInt();
    }
}
