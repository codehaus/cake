package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.forkjoin.collections.ParallelArray;
import org.codehaus.cake.internal.cache.service.memorystore.CacheMap.HashEntry;
import org.codehaus.cake.ops.Ops.ObjectToLong;

public class OpenAdressingMap {
    public static void main(String[] args) {
        ParallelArray<HashEntry<Integer, String>> pa = null;
        pa.withMapping(new ObjectToLong<HashEntry<Integer, String>>() {
            public long op(HashEntry<Integer, String> a) {
                long count = 1;
                return 1;
//                for (HashEntry<Integer, String> e = a; e != null; e = a.next) {
//                    if (filter.op(e)) {
//                        count++;
//                    }
//                }
            }
        });
    }

    static class HashEntry<K, V> implements CacheEntry<K, V> {
        final AttributeMap attributes;

        final int hash;

        final K key;

        final V value;

        HashEntry(K key, int hash, V value, AttributeMap attributes) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.attributes = attributes;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry) o;
            Object key1 = getKey();
            Object key2 = e.getKey();
            if (key1 == key2 || (key1 != null && key1.equals(key2))) {
                Object value1 = getValue();
                Object value2 = e.getValue();
                if (value1 == value2 || (value1 != null && value1.equals(value2)))
                    return true;
            }
            return false;
        }

        public AttributeMap getAttributes() {
            return attributes;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        public final V setValue(V newValue) {
            throw new UnsupportedOperationException("setValue not supported");
        }

        public final String toString() {
            return key + " = " + value;
        }

        @SuppressWarnings("unchecked")
        static final <K, V> HashEntry<K, V>[] newArray(int i) {
            return new HashEntry[i];
        }
    }
}
