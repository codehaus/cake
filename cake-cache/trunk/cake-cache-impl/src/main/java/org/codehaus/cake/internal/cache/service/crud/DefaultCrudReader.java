package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;

public class DefaultCrudReader {

    public static <K> CrudReader<K, Boolean> contains(MemoryStore<K, ?> store) {
        return null;
    }
}
