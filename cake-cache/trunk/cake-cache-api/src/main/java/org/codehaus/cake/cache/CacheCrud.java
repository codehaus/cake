package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ServiceManager;

public class CacheCrud<K, V> {
    protected static AttributeMap READ_VALUE = CrudReader.READ_TRANSFORMER.singleton(CacheDataExtractor.ONLY_VALUE);
    protected static AttributeMap READ_ENTRY = CrudReader.READ_TRANSFORMER.singleton(CacheDataExtractor.WHOLE_ENTRY);

    // protected static AttributeMap WRITE_VOID = CrudReader.WRITE_TRANSFORMER.singleton(null);
    protected static AttributeMap WRITE_RETURN_PREVIOUS_VALUE = CrudWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.ONLY_VALUE);
    protected static AttributeMap WRITE_RETURN_PREVIOUS_ENTRY = CrudWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.WHOLE_ENTRY);

    /** The service manager to extract cache services from. */
    private final ServiceManager serviceManager;

    public CacheCrud(Cache<?, ?> cache) {
        this.serviceManager = cache;
    }

    public CrudReader<K, V> value() {
        return serviceManager.getService(CrudReader.class, READ_VALUE);
    }

    public CrudReader<K, CacheEntry<K, V>> entry() {
        return serviceManager.getService(CrudReader.class, READ_ENTRY);
    }

    public <T> CrudReader<K, T> attribute(Attribute<T> attribute) {
        Op extractor = CacheDataExtractor.attribute(attribute);
        AttributeMap attributes = CrudReader.READ_TRANSFORMER.singleton(extractor);
        return serviceManager.getService(CrudReader.class, attributes);
    }

    public CrudWriter<K, V, Void> write() {
        return serviceManager.getService(CrudWriter.class);
    }

    public CrudWriter<K, V, CacheEntry<K, V>> writeReturnPreviousEntry() {
        return serviceManager.getService(CrudWriter.class, WRITE_RETURN_PREVIOUS_ENTRY);
    }

    public CrudWriter<K, V, V> writeReturnPreviousValue() {
        return serviceManager.getService(CrudWriter.class, WRITE_RETURN_PREVIOUS_VALUE);
    }

    public CrudBatchWriter<K, V, Void> writeBatch() {
        return serviceManager.getService(CrudBatchWriter.class);
    }

    // Async read
    // Timeout
    // CompletionQueue??
    // Det skal være muligt at på en eller anden måde processere data når de bliver loaded.

    // Async callbacks
    // Queue, Procedure<Event>
    // Lets not support async version now??

    // callback mechnism
    // perhaps an attribute with an callback instace
    // DataExtractor, Async(Sync, Queue q, Notifier,....

    // Async::
    // Queue, Procedure<Event>

    // I think I'd prefer,
    // Specify an

    // callback, failed, completed
    // failed, with key + throwable
    // completed either K,V or CacheEntry

    // CrudCreate<K, V, Void> createAsync();

    // CrudCreate<K, V, Future<?>> createAsyncFuture();

    // <T> CrudCreate<K, V, Future<T>> createAsyncFuture(CacheDataExtractor<K, V, T> extractor);

    // CrudCreate<K, V, Void> createAsync(Procedure<?> callback);
}
