/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.Container;

/**
 * A utility class to help creating readers and writers for a cache. An instance of this class is normally acquired by
 * calling {@link Cache#withCrud()}.
 */
@SuppressWarnings("unchecked")
public class CacheCrud<K, V> {

    static AttributeMap WRITE_RETURN_PREVIOUS_ENTRY = CacheWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.WHOLE_ENTRY);
    static AttributeMap WRITE_RETURN_PREVIOUS_VALUE = CacheWriter.WRITE_TRANSFORMER
            .singleton(CacheDataExtractor.ONLY_VALUE);

    /** The contaner to extract cache services from. */
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

    // // TODO Reader default? read Value i Think
    // public CrudReader<K, V> reader(AttributeMap attributes) {
    // return serviceManager.getService(CrudReader.class, attributes);
    // }



    /**
     * Returns a writer that can be used for creating, updating or deleting a single entry.
     * 
     * @return a writer that can be used for creating, updating or deleting a single entry
     */
    public CacheWriter<K, V, Void> write() {
        return serviceManager.getService(CacheWriter.class);
    }

    /**
     * Returns a writer that can be used for creating, updating or deleting multiple items at a time.
     * 
     * @return a writer that can be used for creating, updating or deleting multiple items at a time
     */
    public CacheBatchWriter<K, V, Void> writeBatch() {
        return serviceManager.getService(CacheBatchWriter.class);
    }

    /**
     * Analogues to {@link #write()} except it will return the previous entry that was mapped to the key used when
     * creating, reading or updating entries.
     * 
     * @return a writer that returns the previous entry
     */
    public CacheWriter<K, V, CacheEntry<K, V>> writeReturnPreviousEntry() {
        return serviceManager.getService(CacheWriter.class, WRITE_RETURN_PREVIOUS_ENTRY);
    }

    /**
     * Analogues to {@link #write()} except it will return the previous value that was mapped to the key used when
     * creating, reading or updating entries.
     * 
     * @return a writer that returns the previous value
     */
    public CacheWriter<K, V, V> writeReturnPreviousValue() {
        return serviceManager.getService(CacheWriter.class, WRITE_RETURN_PREVIOUS_VALUE);
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
