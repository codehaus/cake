package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.cache.service.crud.WriteService;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.annotation.ExportAsService;

@ExportAsService(WriteService.class)
public class WriteServiceFactory<K, V> implements ServiceFactory<WriteService> {
    private final CacheRequestFactory<K, V> factory;
    private final CacheProcessor<K, V> processor;

    public WriteServiceFactory(CacheRequestFactory<K, V> factory, CacheProcessor<K, V> processor) {
        this.factory = factory;
        this.processor = processor;
    }

    public WriteService<K, V, ?> lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<WriteService> context) {
        Op op = WriteService.WRITE_TRANSFORMER.get(context);
        return DefaultWriteService.withPrevious(factory, processor, op);
    }
}
