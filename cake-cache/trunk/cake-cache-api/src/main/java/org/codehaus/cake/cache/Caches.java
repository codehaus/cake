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

import static org.codehaus.cake.internal.attribute.AttributeHelper.eq;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;
import org.codehaus.cake.internal.util.CollectionUtils;

/**
 * Various Factory and utility methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServices.java 469 2007-11-17 14:32:25Z kasper $
 */
public final class Caches {

    /**
     * The empty cache (immutable). This cache is serializable.
     * 
     * @see #emptyCache()
     */
    static final Cache EMPTY_CACHE = new EmptyCache();

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Caches() {}

    // /CLOVER:ON
    /**
     * Returns a Runnable that when executed will call the clear method on the specified cache.
     * <p>
     * The following example shows how this can be used to clear the cache every hour.
     * 
     * <pre>
     * Cache c = somecache;
     * ScheduledExecutorService ses = c.with().executors().getScheduledExecutorService();
     * ses.scheduleAtFixedRate(Caches.clearAsRunnable(c), 0, 60 * 60, TimeUnit.SECONDS);
     * 
     * </pre>
     * 
     * @param cache
     *            the cache on which to call evict
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

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> emptyCache() {
        return EMPTY_CACHE;
    }

    public static <K, V> CacheEntry<K, V> newEntry(K key, V value) {
        return newEntry(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public static <K, V> CacheEntry<K, V> newEntry(K key, V value, AttributeMap attributes) {
        return new SimpleImmutableEntry<K, V>(key, value, attributes);
    }

    /**
     * A runnable used for calling clear on a cache.
     */
    static class ClearRunnable implements Runnable {

        /** The cache to call clear on. */
        private final Cache<?, ?> cache;

        /**
         * Creates a new ClearRunnable.
         * 
         * @param cache
         *            the cache to call clear on
         */
        ClearRunnable(Cache<?, ?> cache) {
            if (cache == null) {
                throw new NullPointerException("cache is null");
            }
            this.cache = cache;
        }

        /** {@inheritDoc} */
        public void run() {
            cache.clear();
        }
    }

    static final class CostAttribute extends DoubleAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new CostAttribute. */
        CostAttribute() {
            super("Cost", 1.0);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.COST;
        }
    }

    /**
     * The empty cache.
     */
    static class EmptyCache<K, V> extends AbstractMap<K, V> implements Cache<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -5245003832315997155L;

        /** {@inheritDoc} */
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            return Collections.EMPTY_MAP.entrySet();
        }

        /** {@inheritDoc} */
        public Map<K, V> getAll(Iterable<? extends K> keys) {
            CollectionUtils.checkCollectionForNulls(keys);
            Map<K, V> result = new HashMap<K, V>();
            for (K key : keys) {
                result.put(key, null);
            }
            return result;
        }

        /** {@inheritDoc} */
        public Set<Class<?>> serviceKeySet() {
            return Collections.EMPTY_SET;
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
        public void removeAll(Collection<? extends K> keys) {}

        /** {@inheritDoc} */
        public V replace(K key, V value) {
            throw new UnsupportedOperationException();// ??
        }

        /** {@inheritDoc} */
        public boolean replace(K key, V oldValue, V newValue) {
            return false;
        }

        /** {@inheritDoc} */
        public void shutdown() {}

        /** {@inheritDoc} */
        public void shutdownNow() {}

        /** {@inheritDoc} */
        public CacheServices<K, V> with() {
            return new CacheServices<K, V>(this);
        }
        
        /** {@inheritDoc} */
        public CacheCrud<K, V> withCrud() {
            return new CacheCrud<K, V>(this);
        }
    }

    /**
     * The <tt>Hits</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a type
     * <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class HitsAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        HitsAttribute() {
            super("Hits");
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long hits) {
            return hits >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.HITS;
        }
    }

    static final class SizeAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        SizeAttribute() {
            super("Size", 1);
        }

        /** {@inheritDoc} */
        @Override
        public void checkValid(long value) {
            if (value < 0) {
                throw new IllegalArgumentException("invalid size (size = " + value + ")");
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.SIZE;
        }
    }

    static final class TimeAccessedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeAccessedAttribute() {
            super("AccessTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.TIME_ACCESSED;
        }
    }

    static final class TimeCreatedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeCreatedAttribute() {
            super("CreationTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.TIME_CREATED;
        }
    }

    static final class TimeModificedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeModificedAttribute() {
            super("ModificationTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.TIME_MODIFIED;
        }
    }

    /**
     * This key can be used to indicate how long time an object should live. The time-to-live value should be a long
     * between 1 and {@link Long#MAX_VALUE} measured in nanoseconds. Use {@link java.util.concurrent.TimeUnit} to
     * convert between different time units.
     * 
     */
    // static final class TimeToLiveAttribute extends DurationAttribute {
    //
    // /** serialVersionUID. */
    // private static final long serialVersionUID = -2353351535602223603L;
    //
    // /** The singleton instance of this attribute. */
    // public static final TimeToLiveAttribute TIME_TO_LIVE = new TimeToLiveAttribute();
    //
    // /** Creates a new TimeToLiveAttribute. */
    // TimeToLiveAttribute() {
    // super("TimeToLive");
    // }
    //
    // /** @return Preserves singleton property */
    // private Object readResolve() {
    // return TIME_TO_LIVE;
    // }
    // }
    /**
     * The <tt>Version</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a
     * type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class VersionAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -235335135602223603L;

        /** Creates a new SizeAttribute. */
        VersionAttribute() {
            super("Version", 1);
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long hits) {
            return hits > 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.VERSION;
        }
    }

    /**
     * A CacheEntry maintaining an immutable key and value. This class does not support method <tt>setValue</tt>. This
     * class may be convenient in methods that return thread-safe snapshots of key-value mappings.
     */
    public static class SimpleImmutableEntry<K, V> implements CacheEntry<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The key of the entry. */
        private final K key;

        /** The value of the entry. */
        private final V value;

        /** The attributes of the entry. */
        private final AttributeMap attributes;

        /**
         * Creates an entry representing a mapping from the specified key to the specified value.
         * 
         * @param key
         *            the key represented by this entry
         * @param value
         *            the value represented by this entry
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

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            return eq(key, e.getKey()) && eq(value, e.getValue())/* && eq(attributes, e.getAttributes())*/;
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

        /** {@inheritDoc} */
        public V setValue(V value) {
            throw new UnsupportedOperationException();
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

        public AttributeMap getAttributes() {
            return attributes;
        }
    }
}
