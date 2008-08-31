package org.codehaus.cake.internal.cache.processor.request;

import java.util.Collection;

public interface AddEntriesRequest<K, V> extends Trimable<K, V> {

    Collection<AddEntryRequest<K, V>> adds();
}
