package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.Container;

/**
 * A utility class to help creating, reader, updating and deleting entries in a cache. This class is normally acquired
 * by calling {@link Cache#crud()}.
 */
@SuppressWarnings("unchecked")
public class CacheCrud<K, V> {

    protected static AttributeMap READ_VALUE = CrudReader.READ_TRANSFORMER.singleton(CacheDataExtractor.ONLY_VALUE);
    protected static AttributeMap READ_ENTRY = CrudReader.READ_TRANSFORMER.singleton(CacheDataExtractor.WHOLE_ENTRY);

    // protected static AttributeMap WRITE_VOID = CrudReader.WRITE_TRANSFORMER.singleton(null);
    protected static AttributeMap WRITE_RETURN_PREVIOUS_VALUE = CrudWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.ONLY_VALUE);
    protected static AttributeMap WRITE_RETURN_PREVIOUS_ENTRY = CrudWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.WHOLE_ENTRY);

    /** The service manager to extract cache services from. */
    private final Container serviceManager;

    /**
     * Creates a new CacheCrud from the specified cache.
     * 
     * @param cache
     *            the cache
     */
    public CacheCrud(Cache<?, ?> cache) {
        this.serviceManager = cache;
    }

//    // TODO Reader default? read Value i Think
//    public CrudReader<K, V> reader(AttributeMap attributes) {
//        return serviceManager.getService(CrudReader.class, attributes);
//    }

    /**
     * Returns a {@link CrudReader} that map keys to values
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; cache;
     * //getting a single value (equivalent to cache.get(5)
     * String v = cache.crud().value().get(5);
     * //getting the values for 1,2,3
     * Map&lt;Integer, String&gt; alot = cache.crud().value().getAll(Collections.asList(1,2,3);
     * </pre>
     * 
     * @return a {@link CrudReader} that map keys to values
     */
    public CrudReader<K, V> value() {
        return serviceManager.getService(CrudReader.class, READ_VALUE);
    }

    /**
     * Returns a {@link CrudReader} that map keys to cache entries
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; cache;
     * //getting a single entry (equivalent to cache.getEntry(5)
     * CacheEntry&lt;Integer,String&gt; e = cache.crud().entry().get(5);
     * //getting the entries for 1,2,3
     * Map&lt;Integer, CacheEntry&lt;Integer,String&gt;&gt; alot = cache.crud().entry().getAll(Collections.asList(1,2,3);
     * </pre>
     * 
     * @return a {@link CrudReader} that map keys to values
     */
    public CrudReader<K, CacheEntry<K, V>> entry() {
        return serviceManager.getService(CrudReader.class, READ_ENTRY);
    }

    /**
     * Returns a {@link CrudReader} that map keys to an attribute
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; cache;
     * CrudReader&lt;Integer,Long&gt; reader = cache.crud().attribute(CacheEntry.TIME_CREATED);
     * //get the creation time of the entry mapping from 5
     * long creationTime = reader.get(5);
     * //getting the creation time of the entries from 1,2,3
     * Map&lt;Integer, Long&gt; alot = reader.getAll(Collections.asList(1,2,3);
     * </pre>
     * 
     * @return a {@link CrudReader} that map keys to an attribute
     */
    public <T> CrudReader<K, T> attribute(Attribute<T> attribute) {
        // TODO remember to return default value if mapped to null
        Op extractor = CacheDataExtractor.toAttribute(attribute);
        AttributeMap attributes = CrudReader.READ_TRANSFORMER.singleton(extractor);
        return serviceManager.getService(CrudReader.class, attributes);
    }

    /**
     * Returns a writer that can be used for creating, updating or deleting a single entry.
     * 
     * @return a writer that can be used for creating, updating or deleting a single entry
     */
    public CrudWriter<K, V, Void> write() {
        return serviceManager.getService(CrudWriter.class);
    }

    /**
     * Analogues to {@link #write()} except it will return the previous entry that was mapped to the key used when
     * creating, reading or updating entries.
     * 
     * @return a writer that returns the previous entry
     */
    public CrudWriter<K, V, CacheEntry<K, V>> writeReturnPreviousEntry() {
        return serviceManager.getService(CrudWriter.class, WRITE_RETURN_PREVIOUS_ENTRY);
    }

    /**
     * Analogues to {@link #write()} except it will return the previous value that was mapped to the key used when
     * creating, reading or updating entries.
     * 
     * @return a writer that returns the previous value
     */
    public CrudWriter<K, V, V> writeReturnPreviousValue() {
        return serviceManager.getService(CrudWriter.class, WRITE_RETURN_PREVIOUS_VALUE);
    }

    /**
     * Returns a writer that can be used for creating, updating or deleting multiple items at a time.
     * 
     * @return a writer that can be used for creating, updating or deleting multiple items at a time
     */
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
