package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.service.crud.ReadService;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;

public class DefaultReadService {

    public static <K> ReadService<K, Boolean> contains(MemoryStore<K, ?> store) {
        return null;
    }
}
