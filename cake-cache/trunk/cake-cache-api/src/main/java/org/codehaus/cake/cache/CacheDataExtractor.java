package org.codehaus.cake.cache;

import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops;
import org.codehaus.cake.ops.Ops.Op;

public abstract class CacheDataExtractor<K, V, T> implements Op<CacheEntry<K, V>, T> {
    public static final Op WHOLE_ENTRY = ObjectOps.CONSTANT_OP;

    public static final Op ONLY_KEY = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return e==null ? null : e.getKey();
        }
    };

    public static <K, V, T> Op<CacheEntry<K, V>, T> attribute(Attribute<T> attribute) {
        return new ExtractAttribute<K, V, T>(attribute);
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
            return e==null ? null : e.getValue();
        }
    };

    boolean retainKey;
    boolean retainValue;
    List retainAttributes;

    static class ExtractAttribute<K, V, T> extends CacheDataExtractor<K, V, T> {
        private final Attribute<T> attribute;

        public T op(CacheEntry<K, V> a) {
            if (a==null) {
                return attribute.getDefault();
            }
            return a.getAttributes().get(attribute);
        }

        public ExtractAttribute(Attribute<T> attribute) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
        }
    }
}
