package org.codehaus.cake.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.cache.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.memorystore.openadressing.OpenAdressingMemoryStore;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.service.crud.CrudWriterFactory;
import org.codehaus.cake.internal.cache.service.crud.DefaultCrudBatchWriter;
import org.codehaus.cake.internal.cache.service.crud.DefaultCrudWriter;
import org.codehaus.cake.internal.cache.service.exceptionhandling.DefaultCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.views.CollectionViewFactory;
import org.codehaus.cake.internal.cache.view.DefaultCacheView;
import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.internal.service.AbstractContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedOperation;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;

@ManagedObject(defaultValue = CacheMXBean.MANAGED_SERVICE_NAME, description = "General Cache attributes and operations")
public abstract class AbstractCache<K, V> extends AbstractContainer implements Cache<K, V> {
    private CacheCrud<K, V> crud;

    private Set<Map.Entry<K, V>> entrySet;

    private final Predicate<CacheEntry<K, V>> filter;

    private final CacheProcessor<K, V> processor;
    
    private Set<K> keySet;

    private final MemoryStore<K, V> memoryCache;

    private final CacheRequestFactory<K, V> requestFactory;

    private final CrudWriter<K, V, Boolean> returnPreviousNotNull;
    private final CrudBatchWriter<K, V, Void> returnPreviousNull;

    private final CrudWriter<K, V, V> returnPreviousValue;
    private CacheServices<K, V> services;

    private Collection<V> values;
    /** A factory for collection views. */
    private final CollectionViewFactory<K, V> views;

    AbstractCache(AbstractCache<K, V> parent, Predicate<CacheEntry<K, V>> filter) {
        super(parent);
        views = parent.views;
        memoryCache = parent.memoryCache;
        processor = parent.processor;
        requestFactory = parent.requestFactory;

        returnPreviousValue = DefaultCrudWriter.returnPreviousValue(requestFactory, processor);
        returnPreviousNotNull = DefaultCrudWriter.previousNotNull(requestFactory, processor);
        returnPreviousNull = DefaultCrudBatchWriter.returnVoid(requestFactory, processor);

        this.filter = parent.filter == null ? filter : Predicates.<CacheEntry<K, V>> and(parent.filter, filter);
    }

    AbstractCache(Composer composer) {
        super(composer);
        views = composer.get(CollectionViewFactory.class);
        memoryCache = composer.get(MemoryStore.class);
        processor = composer.get(CacheProcessor.class);
        requestFactory = composer.get(CacheRequestFactory.class);

        returnPreviousValue = DefaultCrudWriter.returnPreviousValue(requestFactory, processor);
        returnPreviousNotNull = DefaultCrudWriter.previousNotNull(requestFactory, processor);
        returnPreviousNull = DefaultCrudBatchWriter.returnVoid(requestFactory, processor);

        filter = null;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Clears the cache")
    public void clear() {
        ClearCacheRequest<K, V> request = requestFactory.createClear();
        processor.process(filter, request);
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        super.lazyStart();
        return memoryCache.containsKey(filter, key);
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        super.lazyStart();
        return memoryCache.containsValue(filter, value);
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
        return (V) processor.get(filter, k, Attributes.EMPTY_ATTRIBUTE_MAP, CacheDataExtractor.ONLY_VALUE);
    }

    /** {@inheritDoc} */
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        return processor.getAll(filter, keys, CacheDataExtractor.ONLY_VALUE);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> getEntry(K key) {
        return (CacheEntry<K, V>) processor.get(filter, key, Attributes.EMPTY_ATTRIBUTE_MAP,
                CacheDataExtractor.WHOLE_ENTRY);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The name of the cache")
    public String getName() {
        return filter == null ? super.getName() : super.getName() + " (Filtered)";
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        MutableAttributeMap map = new DefaultAttributeMap(attributes);
        map.put(InternalCacheAttributes.CONTAINER, this);
        if (filter != null) {
            map.put(InternalCacheAttributes.CACHE_FILTER, filter);
        }
        return super.getService(serviceType, map);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The number of elements contained in the cache")
    public int getSize() {
        return size();
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        super.lazyStart();
        return memoryCache.isEmpty(filter);
    }

    /** {@inheritDoc} */
    public Iterator<CacheEntry<K, V>> iterator() {
        super.lazyStart();
        return memoryCache.iterator(filter);
    }

    /** {@inheritDoc} */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null) ? ks : (keySet = views.keySet(this));
    }

    /** {@inheritDoc} */
    public V peek(K key) {
        super.lazyStart();
        CacheEntry<K, V> e = peekEntry(key);
        return e == null ? null : e.getValue();
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> peekEntry(K key) {
        super.lazyStart();
        return memoryCache.get(filter, key);
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
    public CacheView<K, V> view() {
        return new DefaultCacheView<K, V>(processor, filter);
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
        super.lazyStart();
        return memoryCache.size(filter);
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

    /** {@inheritDoc} */
    public CacheCrud<K, V> withCrud() {
        if (crud == null) {
            crud = new CacheCrud<K, V>(this);
        }
        return crud;
    }

    static Composer newComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = new Composer(Cache.class, configuration);
        //composer.registerImplementation(HashMapMemoryStore.class);
        composer.registerImplementation(ClassDefiner.class);
        composer.registerImplementation(OpenAdressingMemoryStore.class);
        composer.registerInstance(CacheConfiguration.class, configuration);
        composer.registerImplementation(DefaultCacheExceptionService.class);
        composer.registerImplementation(CrudWriterFactory.class);
        return composer;
    }

}
