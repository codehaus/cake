package org.codehaus.cake.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class Maps {

    /** A mapper extracting the key part of a {@link Entry}. The mapper is Serializable. */
    public static final Op MAP_ENTRY_TO_KEY_OP = new KeyFromMapEntry();
    /** A mapper extracting the value part of a {@link Entry}. The mapper is Serializable. */
    public static final Op MAP_ENTRY_TO_VALUE_OP = new ValueFromMapEntry();

    public static final ConcurrentMap EMPTY_CONCURRENTMAP = new EmptyConcurrentMap();

    /**
     * Returns a mapper that extracts the key of any {@link Entry} that will be parsed along to it.
     * 
     * @param <K>
     * @param <V>
     * @return the mapper
     */
    public static <K, V> Op<Map.Entry<K, V>, K> mapEntryToKey() {
        return MAP_ENTRY_TO_KEY_OP;
    }

    public static <K, V> Op<Map.Entry<K, V>, V> mapEntryToValue() {
        return MAP_ENTRY_TO_VALUE_OP;
    }

    /**
     * Returns a new map with only those entries that are accepted by the specified predicate.
     * 
     * @param <K>
     *            the type of keys in the map
     * @param <V>
     *            the type of values in the map
     * @param map
     *            the map to extract entries from
     * @param predicate
     *            the predicate to evaluate entries accordingly to
     * @return a new map with only those entries that are accepted by the specified predicate
     */
    public static <K, V> Map<K, V> filteredMap(Map<K, V> map, Predicate<? super Map.Entry<K, V>> predicate) {
        if (map == null) {
            throw new NullPointerException("map is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        Map<K, V> m = new HashMap<K, V>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.op(entry)) {
                m.put(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    /**
     * Returns a new map with only those entries where the key and value are of the specified types.
     * 
     * @param <K>
     *            the type of keys in the map
     * @param <K1>
     *            the type of keys in the new map
     * @param <V>
     *            the type of values in the map
     * @param <V1>
     *            the type of values in the new map
     * @param map
     *            the map to extract entries from
     * @param keyTypes
     *            the type of keys allowed for the new map
     * @param valueTypes
     *            the type of allowed allowed in the new map
     * @return a new map where all keys and values are of the specified types
     */
    public static <K, K1 extends K, V, V1 extends V> Map<K1, V1> filterMapOnTypes(Map<? extends K, ? extends V> map,
            Class<K1> keyTypes, Class<V1> valueTypes) {
        if (map == null) {
            throw new NullPointerException("map is null");
        } else if (keyTypes == null) {
            throw new NullPointerException("keyTypes is null");
        } else if (valueTypes == null) {
            throw new NullPointerException("valueTypes is null");
        }
        Map<K1, V1> m = new HashMap<K1, V1>();
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key != null && keyTypes.isAssignableFrom(key.getClass()) && value != null
                    && valueTypes.isAssignableFrom(value.getClass()))
                m.put((K1) key, (V1) value);
        }
        return m;
    }

    /**
     * Returns a new map with only those entries where the key is accepted by the specified predicate.
     * 
     * @param <K>
     *            the type of keys in the map
     * @param <V>
     *            the type of values in the map
     * @param map
     *            the map to extract entries from
     * @param predicate
     *            the predicate to evaluate the key of each entry accordingly to
     * @return a new map with only those entries where the key is accepted by the specified predicate
     */
    public static <K, V> Map<K, V> filteredMapOnKeys(Map<K, V> map, Predicate<? super K> predicate) {
        if (map == null) {
            throw new NullPointerException("map is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        Map<K, V> m = new HashMap<K, V>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.op(entry.getKey())) {
                m.put(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    /**
     * Returns a new map with only those entries where the value is accepted by the specified predicate.
     * 
     * @param <K>
     *            the type of keys in the map
     * @param <V>
     *            the type of values in the map
     * @param map
     *            the map to extract entries from
     * @param predicate
     *            the predicate to evaluate the value of each entry accordingly to
     * @return a new map with only those entries where the value is accepted by the specified predicate
     */
    public static <K, V> Map<K, V> filteredMapOnValues(Map<K, V> map, Predicate<? super V> predicate) {
        if (map == null) {
            throw new NullPointerException("map is null");
        } else if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        Map<K, V> m = new HashMap<K, V>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.op(entry.getValue())) {
                m.put(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    public static <K, V> Map<K, V> from(K key, V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> from(K key1, V value1, K key2, V value2) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    /** Extracts the key part of a map entry. */
    static class KeyFromMapEntry<K, V> implements Op<Map.Entry<K, V>, K>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public K op(Map.Entry<K, V> t) {
            return t.getKey();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return Maps.MAP_ENTRY_TO_KEY_OP;
        }
    }

    /** Extracts the value part of a map entry. */
    static class ValueFromMapEntry<K, V> implements Op<Map.Entry<K, V>, V>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public V op(Map.Entry<K, V> t) {
            return t.getValue();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return Maps.MAP_ENTRY_TO_VALUE_OP;
        }
    }

    static class EmptyConcurrentMap<K, V> implements ConcurrentMap<K, V>, Serializable {

        public V putIfAbsent(K key, V value) {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean remove(Object key, Object value) {
            // TODO Auto-generated method stub
            return false;
        }

        public V replace(K key, V value) {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean replace(K key, V oldValue, V newValue) {
            // TODO Auto-generated method stub
            return false;
        }

        public void clear() {
        }

        public boolean containsKey(Object key) {
            return false;
        }

        public boolean containsValue(Object value) {
            return false;
        }

        public Set<Entry<K, V>> entrySet() {
            return Collections.EMPTY_MAP.entrySet();
        }

        public V get(Object key) {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isEmpty() {
            return true;
        }

        public Set<K> keySet() {
            return Collections.EMPTY_MAP.keySet();
        }

        public V put(K key, V value) {
            // TODO Auto-generated method stub
            return null;
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            // TODO Auto-generated method stub

        }

        public V remove(Object key) {
            // TODO Auto-generated method stub
            return null;
        }

        public int size() {
            return 0;
        }

        public Collection<V> values() {
            return Collections.EMPTY_MAP.values();
        }

    }
}
