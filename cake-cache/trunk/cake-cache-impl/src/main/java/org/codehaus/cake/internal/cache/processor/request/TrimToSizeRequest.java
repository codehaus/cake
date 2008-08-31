package org.codehaus.cake.internal.cache.processor.request;

import java.util.Comparator;

public interface TrimToSizeRequest<K, V> extends Trimable<K, V> {
    int getSizeToTrimTo();
    Comparator getComparator();
}
