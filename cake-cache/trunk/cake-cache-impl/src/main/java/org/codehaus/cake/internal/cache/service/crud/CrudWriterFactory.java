package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.CacheBatchWriter;
import org.codehaus.cake.cache.CacheWriter;
import org.codehaus.cake.cache.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.util.ops.Ops.Op;

@ExportAsService( { CacheWriter.class, CacheBatchWriter.class })
public class CrudWriterFactory<K, V> implements ServiceProvider {
    private final CacheRequestFactory<K, V> factory;
    private final CacheProcessor<K, V> processor;
    private final MemoryStoreService<K, V> store;

    public CrudWriterFactory(MemoryStoreService<K, V> store, CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        this.factory = factory;
        this.store = store;
        this.processor = processor;
    }

    public Object lookup(Context context) {
        Class key = context.getKey();
        Op op = context.getAttributes().get(CacheWriter.WRITE_TRANSFORMER);
        if (context.getKey().equals(CacheBatchWriter.class)) {
            return DefaultCrudBatchWriter.returnVoid(factory, processor);
        }
        return DefaultCrudWriter.withPrevious(factory, processor, op);
    }
}
