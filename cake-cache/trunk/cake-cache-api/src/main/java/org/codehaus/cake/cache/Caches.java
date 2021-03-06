/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import static org.codehaus.cake.internal.util.attribute.AttributeHelper.eq;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.util.Maps;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;
import org.codehaus.cake.util.view.MapView;
import org.codehaus.cake.util.view.View;
import org.codehaus.cake.util.view.Views;

/**
 * Various Factory and utility methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public final class Caches {

    /**
     * The empty cache (immutable). This cache is serializable.
     * 
     * @see #emptyCache()
     */
    public static final Cache EMPTY_CACHE = new EmptyCache();

    /** An empty array of cache entries. */
    static final CacheEntry[] EMPTY_CACHE_ENTRY_ARRAY = new CacheEntry[0];

    static final CacheView EMPTY_CACHE_VIEW = new EmptyCacheView();
    /** A CacheSelector that returns the empty cache for all argument. */
    static final CacheSelector EMPTY_SELECTOR = new EmptyCacheSelector();

    /** Cannot instantiate. */
    private Caches() {
    }

    /**
     * Returns a {@link Runnable} that when executed will call the {@link Cache#clear()} method on the specified cache.
     * <p>
     * The following example shows how this can be used to clear the cache every hour.
     * 
     * <pre>
     * Cache c = somecache;
     * ScheduledExecutorService ses = c.with().scheduledExecutor();
     * ses.scheduleAtFixedRate(Caches.clearAsRunnable(c), 0, 60 * 60, TimeUnit.SECONDS);
     * 
     * </pre>
     * 
     * @param cache
     *            the cache on which to call clear
     * @return a runnable where invocation of the run method will clear the specified cache
     * @throws NullPointerException
     *             if the specified cache is <tt>null</tt>.
     */
    public static Runnable clearAsRunnable(Cache<?, ?> cache) {
        return new ClearRunnable(cache);
    }

    /**
     * Returns the empty cache (immutable). This cache is serializable.
     * <p>
     * This example illustrates the type-safe way to obtain an empty cache:
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; c = Caches.emptyCache();
     * </pre>
     * 
     * Implementation note: Implementations of this method need not create a separate <tt>Cache</tt> object for each
     * call. Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the
     * field does not provide type safety.)
     * 
     * @see #EMPTY_CACHE
     */
    public static <K, V> Cache<K, V> emptyCache() {
        return EMPTY_CACHE;
    }

    /**
     * Creates a new CacheEntry with no attributes from the specified key and value.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @return a CacheEntry with the specified key and value
     */
    public static <K, V> CacheEntry<K, V> newEntry(K key, V value) {
        return newEntry(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /**
     * Creates a new CacheEntry from the specified key, value and attribute map.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @param attributes
     *            the attributes
     * @return a CacheEntry with the specified key and value and attributes
     */
    public static <K, V> CacheEntry<K, V> newEntry(K key, V value, AttributeMap attributes) {
        return new SimpleImmutableEntry<K, V>(key, value, attributes);
    }

    /** A runnable used for calling clear on a cache. */
    static class ClearRunnable implements Runnable {

        /** The cache to call clear on. */
        private final Cache<?, ?> map;

        /**
         * Creates a new ClearRunnable.
         * 
         * @param cache
         *            the cache to call clear on
         */
        ClearRunnable(Cache<?, ?> map) {
            if (map == null) {
                throw new NullPointerException("map is null");
            }
            this.map = map;
        }

        /** {@inheritDoc} */
        public void run() {
            map.clear();
        }
    }

    /** The empty cache. */
    static class EmptyCache<K, V> implements Cache<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -5245003832315997155L;

        /** {@inheritDoc} */
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }
        /** {@inheritDoc} */
        public boolean awaitState(State state, long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }
        
        public CacheSelector<K, V> filter() {
            return EMPTY_SELECTOR;
        }

        /** {@inheritDoc} */
        public Map<K, V> getAllOld(Iterable<? extends K> keys) {
            CollectionUtils.checkCollectionForNulls(keys);
            Map<K, V> result = new HashMap<K, V>();
            for (K key : keys) {
                result.put(key, null);
            }
            return result;
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> getEntry(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public String getName() {
            return "emptymap";
        }

        /** {@inheritDoc} */
        public <T> T getService(Class<T> serviceType) {
            return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        /** {@inheritDoc} */
        public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
            if (serviceType == null) {
                throw new NullPointerException("serviceType is null");
            } else if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            throw new UnsupportedOperationException("Unknown service " + serviceType);
        }

        /** {@inheritDoc} */
        public boolean hasService(Class<?> serviceType) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isShutdown() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isStarted() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isTerminated() {
            return false;
        }

        public Iterator<CacheEntry<K, V>> iterator() {
            return Collections.EMPTY_LIST.iterator();
        }

        /** {@inheritDoc} */
        public V peek(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> peekEntry(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public V putIfAbsent(K key, V value) {
            throw new UnsupportedOperationException();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EMPTY_CACHE;
        }

        /** {@inheritDoc} */
        public boolean remove(Object key, Object value) {
            return false;
        }

        /** {@inheritDoc} */
        public V replace(K key, V value) {
            throw new UnsupportedOperationException();// ??
        }

        /** {@inheritDoc} */
        public boolean replace(K key, V oldValue, V newValue) {
            return false;
        }

        /** {@inheritDoc} */
        public Set<Class<?>> serviceKeySet() {
            return Collections.EMPTY_SET;
        }

        /** {@inheritDoc} */
        public void shutdown() {
        }

        /** {@inheritDoc} */
        public void shutdownNow() {
        }

        public CacheView<K, V> view() {
            return EMPTY_CACHE_VIEW;
        }

        /** {@inheritDoc} */
        public CacheServices<K, V> with() {
            return new CacheServices<K, V>(this);
        }

        /** {@inheritDoc} */
        public CacheCrud<K, V> withCrud() {
            return new CacheCrud<K, V>(this);
        }

        public CacheView<K, V> getAll(Iterable<? extends K> keys) {
            return EMPTY_CACHE_VIEW;
        }

        public ConcurrentMap<K, V> asMap() {
            return Maps.EMPTY_CONCURRENTMAP;
        }

        public void clear() {
        }

        public boolean containsKey(Object key) {
            return false;
        }

        public boolean containsValue(Object value) {
            return false;
        }

        public V get(Object key) {
            return null;
        }

        public boolean isEmpty() {
            return true;
        }

        public V put(K key, V value) {
            return (V) Collections.emptyMap().put(key, value);
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            Collections.EMPTY_MAP.putAll(m);
        }

        public V remove(Object key) {
            return (V) Collections.emptyMap().remove(key);
        }

        public long size() {
            return 0;
        }

        public State getState() {
            return State.RUNNING;
        }
    }


    /** A cache selector that always returns the empty cache. */
    static class EmptyCacheSelector<K, V> implements CacheSelector<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public <T> Cache<K, V> on(Attribute<T> a, Predicate<T> p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(BinaryPredicate<? super K, ? super V> selector) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(BooleanAttribute a, boolean value) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(ByteAttribute a, BytePredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(CharAttribute a, CharPredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(DoubleAttribute a, DoublePredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(FloatAttribute a, FloatPredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(IntAttribute a, IntPredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(LongAttribute a, LongPredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(Predicate<CacheEntry<K, V>> p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> on(ShortAttribute a, ShortPredicate p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> onKey(Predicate<? super K> p) {
            return EMPTY_CACHE;
        }

        public <T extends K> Cache<T, V> onKeyType(Class<T> p) {
            return EMPTY_CACHE;
        }

        public Cache<K, V> onValue(Predicate<? super V> p) {
            return EMPTY_CACHE;
        }

        public <T extends V> Cache<K, T> onValueType(Class<T> clazz) {
            return EMPTY_CACHE;
        }
    }

    static class EmptyCacheView<K, V> implements CacheView<K, V>, Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        public View<CacheEntry<K, V>> entries() {
            return Views.EMPTY_VIEW;
        }

        public View<K> keys() {
            return Views.EMPTY_VIEW;
        }

        public MapView<K, V> keysValues() {
            return Views.EMPTY_MAP_VIEW;
        }

        public CacheView<K, V> orderBy(Comparator<? super CacheEntry<K, V>> comparator) {
            return this;
        }

        public <T> CacheView<K, V> orderByAttribute(Attribute<T> attribute, Comparator<? extends T> comparator) {
            return this;
        }

        public CacheView<K, V> orderByAttributeMax(Attribute<?> attribute) {
            return this;
        }

        public CacheView<K, V> orderByAttributeMin(Attribute<?> attribute) {
            return this;
        }

        public CacheView<K, V> orderByKeys(Comparator<K> comparator) {
            return this;
        }

        public CacheView<K, V> orderByKeysMax() {
            return this;
        }

        public CacheView<K, V> orderByKeysMin() {
            return this;
        }

        public CacheView<K, V> orderByValues(Comparator<V> comparator) {
            return this;
        }

        public CacheView<K, V> orderByValuesMax() {
            return this;
        }

        public CacheView<K, V> orderByValuesMin() {
            return this;
        }

        public CacheView<K, V> setLimit(long limit) {
            return this;
        }

        public CacheEntry<K, V>[] toArray() {
            return EMPTY_CACHE_ENTRY_ARRAY;
        }

        public View<V> values() {
            return Views.EMPTY_VIEW;
        }
    }

    /**
     * A CacheEntry maintaining an immutable key and value. This class does not support method <tt>setValue</tt>. This
     * class may be convenient in methods that return thread-safe snapshots of key-value mappings.
     */
    static class SimpleImmutableEntry<K, V> implements CacheEntry<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The attributes of the entry. */
        private final AttributeMap attributes;

        /** The key of the entry. */
        private final K key;

        /** The value of the entry. */
        private final V value;

        /**
         * Creates an entry representing a mapping from the specified key to the specified value.
         * 
         * @param key
         *            the key represented by this entry
         * @param value
         *            the value represented by this entry
         * @throws NullPointerException
         *             if the specified key, value or attribute map is null
         */
        public SimpleImmutableEntry(K key, V value, AttributeMap attributes) {
            if (key == null) {
                throw new NullPointerException("key is null");
            } else if (value == null) {
                throw new NullPointerException("value is null");
            } else if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            this.key = key;
            this.value = value;
            this.attributes = attributes;
        }

        public Set<Attribute> attributes() {
            return attributes.attributes();
        }

        public boolean contains(Attribute<?> attribute) {
            return attributes.contains(attribute);
        }

        public Set<Entry<Attribute, Object>> entrySet() {
            return attributes.entrySet();
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            return eq(key, e.getKey()) && eq(value, e.getValue())/* && eq(attributes, e.getAttributes()) */;
        }

        public <T> T get(Attribute<T> attribute) {
            return attributes.get(attribute);
        }

        public <T> T get(Attribute<T> attribute, T defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public boolean get(BooleanAttribute attribute) {
            return attributes.get(attribute);
        }

        public boolean get(BooleanAttribute attribute, boolean defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public byte get(ByteAttribute attribute) {
            return attributes.get(attribute);
        }

        public byte get(ByteAttribute attribute, byte defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public char get(CharAttribute attribute) {
            return attributes.get(attribute);
        }

        public char get(CharAttribute attribute, char defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public double get(DoubleAttribute attribute) {
            return attributes.get(attribute);
        }

        public double get(DoubleAttribute attribute, double defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public float get(FloatAttribute attribute) {
            return attributes.get(attribute);
        }

        public float get(FloatAttribute attribute, float defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public int get(IntAttribute attribute) {
            return attributes.get(attribute);
        }

        public int get(IntAttribute attribute, int defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public long get(LongAttribute attribute) {
            return attributes.get(attribute);
        }

        public long get(LongAttribute attribute, long defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public short get(ShortAttribute attribute) {
            return attributes.get(attribute);
        }

        public short get(ShortAttribute attribute, short defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        /** {@inheritDoc} */
        public K getKey() {
            return key;
        }

        /** {@inheritDoc} */
        public V getValue() {
            return value;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        public boolean isEmpty() {
            return attributes.isEmpty();
        }

        /** {@inheritDoc} */
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return attributes.size();
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append(" [");

            Iterator<Map.Entry<Attribute, Object>> i = attributes.entrySet().iterator();
            if (!i.hasNext()) {
                return sb.append("]").toString();
            }
            for (;;) {
                Map.Entry<Attribute, Object> e = i.next();
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                if (!i.hasNext())
                    return sb.append(']').toString();
                sb.append(", ");
            }
        }

        public Collection<Object> values() {
            return attributes.values();
        }
    }
}
