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

import java.util.ArrayList;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.cache.CacheEntry;

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractArrayReplacementPolicy<K, V> extends AbstractReplacementPolicy<K, V> {
    private final IntAttribute index = new IntAttribute("index", 0) {};
    private final ArrayList<CacheEntry<K, V>> list = new ArrayList<CacheEntry<K, V>>();

    public AbstractArrayReplacementPolicy() {
        attachToEntry(index);
    }

    public boolean add(CacheEntry<K, V> entry) {
        add0(entry);
        return true;
    }

    protected int add0(CacheEntry<K, V> entry) {
        int i = list.size();
        index.set(entry, i);
        list.add(entry);
        return i;
    }

    public void clear() {
        list.clear();
    }

    protected CacheEntry<K, V> getFromIndex(int index) {
        return list.get(index);
    }

    protected int getIndexOf(CacheEntry<K, V> entry) {
        return index.get(entry);
    }

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
            int i = index.get(entry);
            list.set(i, last);
            index.set(last, i);
            swap(lastIndex, i);
        }
        return lastIndex;
    }

    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        replace0(previous, newEntry);
        return newEntry;
    }

    protected void replace0(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        int i = index.get(previous);
        index.set(newEntry, i);
        list.set(i, newEntry);
    }

    protected int size() {
        return list.size();
    }

    protected void swap(int prevIndex, int newIndex) {}
}
