package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;

public class DefaultTrimToSizeRequest<K, V> implements TrimToSizeRequest<K, V> {

    private int sizeToTrimTo;
    private Comparator comparator;

    public DefaultTrimToSizeRequest(long sizeToTrimTo, Comparator comperator) {
        this.sizeToTrimTo = (int) sizeToTrimTo;
        this.comparator = comperator;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public int getSizeToTrimTo() {
        return sizeToTrimTo;
    }

    public boolean doTrim() {
        return false;
    }
    private List<CacheEntry<K, V>> trimmed;

    public List<CacheEntry<K, V>> getTrimmed() {
        return trimmed;
    }

    public void setTrimmed(List<CacheEntry<K, V>> trimmed) {
        this.trimmed = trimmed;
    }

}
