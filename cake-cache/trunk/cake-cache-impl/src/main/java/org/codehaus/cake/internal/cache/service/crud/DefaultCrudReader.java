package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultCrudReader<K, V> {
    private Predicate<CacheEntry<K, V>> predicate;

    public static <K> CrudReader<K, Boolean> contains(MemoryStore<K, ?> store) {
        return null;
    }
}
