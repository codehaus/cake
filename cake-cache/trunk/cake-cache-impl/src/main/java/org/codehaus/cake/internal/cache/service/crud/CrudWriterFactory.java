package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.annotation.ExportAsService;

@ExportAsService( { CrudReader.class, CrudWriter.class, CrudBatchWriter.class })
public class CrudWriterFactory<K, V> implements ServiceFactory {
    private final CacheRequestFactory<K, V> factory;
    private final CacheProcessor<K, V> processor;
    private final MemoryStoreService<K, V> store;

    public CrudWriterFactory(MemoryStoreService<K, V> store, CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        this.factory = factory;
        this.store = store;
        this.processor = processor;
    }

    public Object lookup(ServiceFactoryContext context) {
        Class key = context.getKey();
        if (key.equals(CrudReader.class)) {
            Predicate p = InternalCacheAttributes.CACHE_FILTER.get(context);
            Op op = CrudReader.READ_TRANSFORMER.get(context);
            return new DefaultCrudReader(p, processor, op);
        }
        Op op = CrudWriter.WRITE_TRANSFORMER.get(context);
        if (context.getKey().equals(CrudBatchWriter.class)) {
            DefaultCrudBatchWriter.returnVoid(factory, processor);
        }
        return DefaultCrudWriter.withPrevious(factory, processor, op);
    }
}
