package org.codehaus.cake.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.cache.service.crud.WriteService;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.service.crud.DefaultWriteService;
import org.codehaus.cake.internal.cache.service.crud.WriteServiceFactory;
import org.codehaus.cake.internal.cache.service.exceptionhandling.DefaultCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.ExportedMemoryStoreService;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.CollectionViews;
import org.codehaus.cake.internal.service.AbstractContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedObject;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * This class provides a skeletal implementation of the <tt>Cache</tt> interface, to minimize the effort required to
 * implement this interface.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: SynchronizedCache.java 560 2008-01-09 16:58:56Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
@ManagedObject(defaultValue = CacheMXBean.MANAGED_SERVICE_NAME, description = "General Cache attributes and operations")
public abstract class AbstractCache<K, V> extends AbstractContainer implements Cache<K, V>, Iterable<CacheEntry<K, V>>,
        CacheMXBean {

    final MemoryStore<K, V> memoryCache;

    private final CacheProcessor<K, V> processor;

    private final CacheRequestFactory<K, V> requestFactory;

    private final WriteService<K, V, Boolean> returnPreviousNotNull;

    private final WriteService<K, V, V> returnPreviousValue;
    
    private CacheServices<K, V> services;
    private CacheCrud<K, V> crud;
    /** Object containing the various collection views. */
    private final CollectionViews<K, V> views;

    /**
     * Creates a new AbstractCache.
     * 
     * @param composer
     *            the composer used for retrieving services
     */
    AbstractCache(Composer composer) {
        super(composer);
        memoryCache = composer.get(MemoryStore.class);
        views = composer.get(CollectionViews.class);
        requestFactory = composer.get(CacheRequestFactory.class);
        processor = composer.get(CacheProcessor.class);
        returnPreviousValue = DefaultWriteService.returnPreviousValue(requestFactory, processor);
        returnPreviousNotNull = DefaultWriteService.previousNotNull(requestFactory, processor);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Clears the cache")
    public void clear() {
        ClearCacheRequest<K, V> request = requestFactory.createClear();
        processor.process(request);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public boolean containsKey(Object key) {
        return peek((K) key) != null;
    }

    /** {@inheritDoc} */
    public Set<Entry<K, V>> entrySet() {
        return views.entrySet();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        K k = (K) key;
        return (V) processor.get(k, CacheDataExtractor.ONLY_VALUE, false);
    }

    /** {@inheritDoc} */
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        return processor.getAll(keys, CacheDataExtractor.ONLY_VALUE, false);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> getEntry(K key) {
        return (CacheEntry<K, V>) processor.get(key, CacheDataExtractor.WHOLE_ENTRY, false);
    }

    /** {@inheritDoc} */
    @Override
    @ManagedAttribute(description = "The name of the cache")
    public String getName() {
        return super.getName();
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The number of elements contained in the cache")
    public int getSize() {
        return size();
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** {@inheritDoc} */
    public Set<K> keySet() {
        return views.keySet();
    }

    /** {@inheritDoc} */
    public V peek(K key) {
        return (V) processor.get(key, CacheDataExtractor.ONLY_VALUE, true);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> peekEntry(K key) {
        return (CacheEntry<K, V>) processor.get(key, CacheDataExtractor.WHOLE_ENTRY, true);
    }

    /** {@inheritDoc} */
    public void prestart() {
        size();
    }

    /** {@inheritDoc} */
    public V put(K key, V value) {
        return returnPreviousValue.put(key, value);
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends K, ? extends V> t) {
        returnPreviousValue.putAll(t);
    }

    /** {@inheritDoc} */
    public V putIfAbsent(K key, V value) {
        return returnPreviousValue.putIfAbsent(key, value);
    }

    /** {@inheritDoc} */
    public V remove(Object key) {
        return returnPreviousValue.remove((K) key);
    }

    /** {@inheritDoc} */
    public boolean remove(Object key, Object value) {
        K k = (K) key;
        V v = (V) value;
        return returnPreviousNotNull.remove(k, v).booleanValue();
    }

    /** {@inheritDoc} */
    public void removeAll(Collection<? extends K> keys) {
        RemoveEntriesRequest<K, V> request = requestFactory.removeAll(keys);
        processor.process(request);
    }

    /** {@inheritDoc} */
    public V replace(K key, V value) {
        return returnPreviousValue.replace(key, value);
    }

    /** {@inheritDoc} */
    public boolean replace(K key, V oldValue, V newValue) {
        return returnPreviousNotNull.replace(key, oldValue, newValue).booleanValue();
    }

    /** {@inheritDoc} */
    public int size() {
        return with().memoryStore().getSize();
    }

    /** {@inheritDoc} */
    public String toString() {
        if (!isStarted()) {
            return "{}";
        }
        Iterator<Entry<K, V>> i = entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Cache)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Cache)" : value);
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(", ");
        }
    }

    /** {@inheritDoc} */
    public Collection<V> values() {
        return views.values();
    }

    /** {@inheritDoc} */
    public CacheServices<K, V> with() {
        if (services == null) {
            services = new CacheServices<K, V>(this);
        }
        return services;
    }
    /** {@inheritDoc} */
    public CacheCrud<K, V> withCrud() {
        if (crud == null) {
            crud = new CacheCrud<K, V>(this);
        }
        return crud;
    }
    static Composer newComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = new Composer(Cache.class, configuration);
        composer.registerImplementation(ExportedMemoryStoreService.class);
        composer.registerInstance(CacheConfiguration.class, configuration);
        composer.registerImplementation(DefaultCacheExceptionService.class);
        composer.registerImplementation(WriteServiceFactory.class);
        return composer;
    }
}
