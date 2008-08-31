package org.codehaus.cake.internal.cache.processor.request;

import java.util.Collection;

public interface RemoveEntriesRequest<K, V> {
    Collection<RemoveEntryRequest<K, V>> removes();
}
