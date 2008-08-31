package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;

public class DefaultTrimToVolumeRequest<K, V> implements TrimToVolumeRequest<K, V> {

    private long volumeToTrimTo;
    private Comparator comparator;

    public DefaultTrimToVolumeRequest(long volumeToTrimTo, Comparator comperator) {
        this.volumeToTrimTo = volumeToTrimTo;
        this.comparator = comperator;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public long getVolumeToTrimTo() {
        return volumeToTrimTo;
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
