package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.annotation.ExportAsService;

@ExportAsService( { CrudWriter.class, CrudBatchWriter.class })
public class CrudWriterFactory<K, V> implements ServiceFactory {
    private final CacheRequestFactory<K, V> factory;
    private final CacheProcessor<K, V> processor;

    public CrudWriterFactory(CacheRequestFactory<K, V> factory, CacheProcessor<K, V> processor) {
        this.factory = factory;
        this.processor = processor;
    }

    public Object lookup(ServiceFactoryContext context) {
        Op op = CrudWriter.WRITE_TRANSFORMER.get(context);
        if (context.getKey().equals(CrudBatchWriter.class)) {
            DefaultCrudBatchWriter.returnVoid(factory, processor);
        }
        return DefaultCrudWriter.withPrevious(factory, processor, op);
    }
}
