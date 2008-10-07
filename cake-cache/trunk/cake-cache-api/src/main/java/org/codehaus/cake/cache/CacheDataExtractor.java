package org.codehaus.cake.cache;

import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops;
import org.codehaus.cake.ops.Ops.Op;

public class CacheDataExtractor<K, V, T> implements Op<CacheEntry<K, V>, T> {
    public static final Op WHOLE_ENTRY = ObjectOps.CONSTANT_OP;

    public static final Op ONLY_KEY = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return e.getKey();
        }
    };

    public static <K, V, T> Op<CacheEntry<K, V>, T> attribute(Attribute<T> attribute) {
        return null;
    }
    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValue(Op<V, T> op) {
        return null;
    }
    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValueBean(String name) {
        return null;
    }
    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromKey(Op<K, T> op) {
        return null;
    }
    public static final Op ONLY_VALUE = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return e.getValue();
        }
    };

    boolean retainKey;
    boolean retainValue;
    List retainAttributes;

    public T op(CacheEntry<K, V> a) {
        // TODO Auto-generated method stub
        return null;
    }
}
