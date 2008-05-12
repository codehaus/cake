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
    private ArrayList<CacheEntry<K, V>> list = new ArrayList<CacheEntry<K, V>>();

    public AbstractArrayReplacementPolicy() {
        attachToEntry(index);
    }

    public boolean add(CacheEntry<K, V> entry) {
        addAndReturnIndex(entry);
        return true;
    }

    protected int addAndReturnIndex(CacheEntry<K, V> entry) {
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
        removeAndReturnIndex(entry);
    }

    protected CacheEntry<K, V> removeByIndex(int index) {
        CacheEntry<K, V> entry = list.get(index);
        removeAndReturnIndex(entry);
        return entry;
    }

    protected int removeAndReturnIndex(CacheEntry<K, V> entry) {
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
        replaceIt(previous, newEntry);
        return newEntry;
    }

    protected void replaceIt(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        int i = index.get(previous);
        index.set(newEntry, i);
        list.set(i, newEntry);
    }

    protected int size() {
        return list.size();
    }

    protected void swap(int prevIndex, int newIndex) {}
}
