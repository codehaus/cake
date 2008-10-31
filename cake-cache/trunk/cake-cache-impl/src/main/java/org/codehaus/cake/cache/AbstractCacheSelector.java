package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.ops.Ops.BinaryPredicate;
import org.codehaus.cake.ops.Ops.BytePredicate;
import org.codehaus.cake.ops.Ops.CharPredicate;
import org.codehaus.cake.ops.Ops.DoublePredicate;
import org.codehaus.cake.ops.Ops.FloatPredicate;
import org.codehaus.cake.ops.Ops.IntPredicate;
import org.codehaus.cake.ops.Ops.LongPredicate;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.ShortPredicate;

abstract class AbstractCacheSelector<K, V> implements CacheSelector<K, V> {

    public <T> Cache<K, V> on(final Attribute<T> attribute, final Predicate<T> filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                T t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final BinaryPredicate<? super K, ? super V> filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K key = a.getKey();
                V value = a.getValue();
                return filter.op(key, value);
            }
        });
    }

    public Cache<K, V> on(final BooleanAttribute attribute, final boolean value) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                boolean t = a.getAttributes().get(attribute);
                return t == value;
            }
        });
    }

    public Cache<K, V> on(final ByteAttribute attribute, final BytePredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                byte t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final CharAttribute attribute, final CharPredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                char t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final DoubleAttribute attribute, final DoublePredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                double t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final FloatAttribute attribute, final FloatPredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                float t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final IntAttribute attribute, final IntPredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                int t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final LongAttribute attribute, final LongPredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                long t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> on(final ShortAttribute attribute, final ShortPredicate filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                short t = a.getAttributes().get(attribute);
                return filter.op(t);
            }
        });
    }

    public Cache<K, V> onKey(final Predicate<K> filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K k = a.getKey();
                return filter.op(k);
            }
        });
    }

    public <T extends K> Cache<T, V> onKeyType(final Class<T> clazz) {
        return (Cache) on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K k = a.getKey();
                return clazz.isAssignableFrom(k.getClass());
            }
        });
    }

    public Cache<K, V> onValue(final Predicate<V> filter) {
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                V v = a.getValue();
                return filter.op(v);
            }
        });
    }

    public <T extends V> Cache<K, T> onValueType(final Class<T> clazz) {
        return (Cache) on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                V v = a.getValue();
                return clazz.isAssignableFrom(v.getClass());
            }
        });
    }

}
