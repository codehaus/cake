package org.codehaus.cake.internal.cache.processor.request;

import java.util.Comparator;

public interface TrimToVolumeRequest<K, V> extends Trimable<K, V> {
    long getVolumeToTrimTo();
    Comparator getComparator();
}
