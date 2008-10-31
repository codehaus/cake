package org.codehaus.cake.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.DefaultCacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.service.crud.CrudWriterFactory;
import org.codehaus.cake.internal.cache.service.crud.DefaultCrudBatchWriter;
import org.codehaus.cake.internal.cache.service.crud.DefaultCrudWriter;
import org.codehaus.cake.internal.cache.service.exceptionhandling.DefaultCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.ExportedMemoryStoreService;
import org.codehaus.cake.internal.cache.service.memorystore.HashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.CollectionViewFactory;
import org.codehaus.cake.internal.service.AbstractContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedObject;
import org.codehaus.cake.management.annotation.ManagedOperation;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;

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

    private CacheCrud<K, V> crud;

    private Set<Map.Entry<K, V>> entrySet;

    private Set<K> keySet;

    final MemoryStore<K, V> memoryCache;
    final ExportedMemoryStoreService<K, V> exportedMemoryStore;
    private final CacheProcessor<K, V> processor;
    private final CacheRequestFactory<K, V> requestFactory;
    private final CrudWriter<K, V, Boolean> returnPreviousNotNull;

    private final CrudBatchWriter<K, V, Void> returnPreviousNull;
    private final CrudWriter<K, V, V> returnPreviousValue;

    private CacheServices<K, V> services;

    private Collection<V> values;
    /** Object containing the various collection views. */
    private final CollectionViewFactory<K, V> views;

    /**
     * Creates a new AbstractCache.
     * 
     * @param composer
     *            the composer used for retrieving services
     */
    AbstractCache(Composer composer) {
        super(composer);
        memoryCache = composer.get(MemoryStore.class);
        views = composer.get(CollectionViewFactory.class);
        requestFactory = composer.get(CacheRequestFactory.class);
        processor = composer.get(CacheProcessor.class);
        exportedMemoryStore=composer.get(ExportedMemoryStoreService.class);
        returnPreviousValue = DefaultCrudWriter.returnPreviousValue(requestFactory, processor);
        returnPreviousNotNull = DefaultCrudWriter.previousNotNull(requestFactory, processor);
        returnPreviousNull = DefaultCrudBatchWriter.returnVoid(requestFactory, processor);
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
    public CacheCrud<K, V> crud() {
        if (crud == null) {
            crud = new CacheCrud<K, V>(this);
        }
        return crud;
    }

    /** {@inheritDoc} */
    public Set<Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = entrySet;
        return (es != null) ? es : (entrySet = views.entrySet(this));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        K k = (K) key;
        return (V) processor.get(null, k, CacheDataExtractor.ONLY_VALUE, false);
    }

    /** {@inheritDoc} */
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        return processor.getAll(null, keys, CacheDataExtractor.ONLY_VALUE, false);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> getEntry(K key) {
        return (CacheEntry<K, V>) processor.get(null, key, CacheDataExtractor.WHOLE_ENTRY, false);
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
        Set<K> ks = keySet;
        return (ks != null) ? ks : (keySet = views.keySet(this));
    }

    /** {@inheritDoc} */
    public V peek(K key) {
        return (V) processor.get(null, key, CacheDataExtractor.ONLY_VALUE, true);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> peekEntry(K key) {
        return (CacheEntry<K, V>) processor.get(null, key, CacheDataExtractor.WHOLE_ENTRY, true);
    }

    /** {@inheritDoc} */
    public void prestart() {
        serviceKeySet();
    }

    /** {@inheritDoc} */
    public V put(K key, V value) {
        return returnPreviousValue.put(key, value);
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends K, ? extends V> t) {
        returnPreviousNull.putAll(t);
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
    public V replace(K key, V value) {
        return returnPreviousValue.replace(key, value);
    }

    /** {@inheritDoc} */
    public boolean replace(K key, V oldValue, V newValue) {
        return returnPreviousNotNull.replace(key, oldValue, newValue).booleanValue();
    }

    /** {@inheritDoc} */
    public int size() {
        serviceKeySet();
        return exportedMemoryStore.getSize();
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
        Collection<V> vs = values;
        return (vs != null) ? vs : (values = views.values(this));
    }

    /** {@inheritDoc} */
    public CacheServices<K, V> with() {
        if (services == null) {
            services = new CacheServices<K, V>(this);
        }
        return services;
    }
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        AttributeMap map = new DefaultAttributeMap(attributes);
        map.put(InternalCacheAttributes.CONTAINER, this);
        return super.getService(serviceType, map);
    }

    static Composer newComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = new Composer(Cache.class, configuration);
        composer.registerImplementation(HashMapMemoryStore.class);
        composer.registerInstance(CacheConfiguration.class, configuration);
        composer.registerImplementation(DefaultCacheExceptionService.class);
        composer.registerImplementation(CrudWriterFactory.class);
        return composer;
    }

    abstract class AbstractSelectedCache implements Cache<K, V> {
        private CacheCrud<K, V> crud;
        private Set<Map.Entry<K, V>> entrySet;

        final Predicate<CacheEntry<K, V>> filter;
        private final CacheRequestFactory<K, V> requestFactory;

        private Set<K> keySet;

        private CacheServices<K, V> services;
        private Collection<V> values;

        AbstractSelectedCache(Predicate<CacheEntry<K, V>> filter) {
            this.filter = filter;
            requestFactory = new DefaultCacheRequestFactory<K, V>(filter);
        }
        /** {@inheritDoc} */
        public Iterator<CacheEntry<K, V>> iterator() {
            lazyStart();
            return memoryCache.iterator(filter);
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return AbstractCache.this.awaitTermination(timeout, unit);
        }

        public void clear() {
            ClearCacheRequest<K, V> request = requestFactory.createClear();
            processor.process(request);
        }
        /** {@inheritDoc} */
        public int size() {
            return memoryCache.size(filter);
        }
        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public boolean containsKey(Object key) {
            return peek((K) key) != null;
        }

        public CacheCrud<K, V> crud() {
            if (crud == null) {
                crud = new CacheCrud<K, V>(this);
            }
            return crud;
        }

        /** {@inheritDoc} */
        public Set<Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> es = entrySet;
            return (es != null) ? es : (entrySet = views.entrySet(this));
        }

        public String getName() {
            return AbstractCache.this.getName() + " (Filtered View)";
        }

        public <T> T getService(Class<T> serviceType) {
            return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
            AttributeMap map = new DefaultAttributeMap(attributes);
            map.put(InternalCacheAttributes.CACHE_FILTER, filter);
            map.put(InternalCacheAttributes.CONTAINER, this);
            return AbstractCache.this.getService(serviceType, map);
        }

        public boolean hasService(Class<?> serviceType) {
            return AbstractCache.this.hasService(serviceType);
        }

        public boolean isShutdown() {
            return AbstractCache.this.isShutdown();
        }

        public boolean isStarted() {
            return AbstractCache.this.isStarted();
        }

        public boolean isTerminated() {
            return AbstractCache.this.isTerminated();
        }

        /** {@inheritDoc} */
        public Set<K> keySet() {
            Set<K> ks = keySet;
            return (ks != null) ? ks : (keySet = views.keySet(this));
        }

        /** {@inheritDoc} */
        public V peek(K key) {
            return (V) processor.get(filter, key, CacheDataExtractor.ONLY_VALUE, true);
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> peekEntry(K key) {
            return (CacheEntry<K, V>) processor.get(filter, key, CacheDataExtractor.WHOLE_ENTRY, true);
        }
        public Set<Class<?>> serviceKeySet() {
            return AbstractCache.this.serviceKeySet();
        }

        public void shutdown() {
            throw new UnsupportedOperationException("Cannot shutdown filtered view, must shutdown originally cache");
        }

        public void shutdownNow() {
            throw new UnsupportedOperationException("Cannot shutdown filtered view, must shutdown originally cache");
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
        @SuppressWarnings("unchecked")
        public V get(Object key) {
            K k = (K) key;
            return (V) processor.get(filter, k, CacheDataExtractor.ONLY_VALUE, false);
        }

        /** {@inheritDoc} */
        public Map<K, V> getAll(Iterable<? extends K> keys) {
            return processor.getAll(filter, keys, CacheDataExtractor.ONLY_VALUE, false);
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> getEntry(K key) {
            return (CacheEntry<K, V>) processor.get(filter, key, CacheDataExtractor.WHOLE_ENTRY, false);
        }


        /** {@inheritDoc} */
        public Collection<V> values() {
            Collection<V> vs = values;
            return (vs != null) ? vs : (values = views.values(this));
        }

        /** {@inheritDoc} */
        public CacheServices<K, V> with() {
            if (services == null) {
                services = new CacheServices<K, V>(this);
            }
            return services;
        }
        public boolean containsValue(Object value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public V put(K key, V value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public void putAll(Map<? extends K, ? extends V> m) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public V putIfAbsent(K key, V value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public boolean remove(Object key, Object value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public V remove(Object key) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public boolean replace(K key, V oldValue, V newValue) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public V replace(K key, V value) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
        public CacheSelector<K, V> select() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
    }
}
