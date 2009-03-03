package org.codehaus.cake.internal.cache.view;

import java.lang.reflect.Array;
import java.util.Comparator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheView;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.collection.MapView;
import org.codehaus.cake.util.collection.View;
import org.codehaus.cake.util.ops.Comparators;
import org.codehaus.cake.util.ops.ObjectOps;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class DefaultCacheView<K, V> extends AbstractView implements CacheView<K, V> {

    private DefaultCacheView(AbstractView parent, Object command) {
        super(parent, command);
    }

    private DefaultCacheView(AbstractView parent, Object... commands) {
        super(parent, commands);
    }

    /** {@inheritDoc} */
    public DefaultCacheView(CacheProcessor executor, Predicate filter) {
        super(executor, filter);
    }

    /** {@inheritDoc} */
    public View<CacheEntry<K, V>> entries() {
        return new DefaultView<CacheEntry<K, V>>(this, ObjectOps.CONSTANT_OP);
    }

    /** {@inheritDoc} */
    public View<K> keys() {
        return new DefaultView<K>(this, ViewCommands.MAP_TO_KEYS);
    }

    /** {@inheritDoc} */
    public MapView<K, V> keysValues() {
        return new DefaultMapView<K, V>(this, ViewCommands.MAP_TO_MAP_ENTRIES);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderBy(Comparator<? super CacheEntry<K, V>> comparator) {
        return new DefaultCacheView<K, V>(this, comparator);
    }

    /** {@inheritDoc} */
    public View<V> values() {
        return new DefaultView<V>(this, ViewCommands.MAP_TO_VALUES);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByKeys(Comparator<K> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_KEY, comparator);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByKeysMax() {
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_KEY, Comparators.NATURAL_COMPARATOR);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByKeysMin() {
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_KEY, ViewCommands.ORDER_BY_NATURAL_MIN);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByValues(Comparator<V> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_VALUE, comparator);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByValuesMax() {
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_VALUE, Comparators.NATURAL_COMPARATOR);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByValuesMin() {
        return new DefaultCacheView<K, V>(this, ViewCommands.ORDER_BY_VALUE, ViewCommands.ORDER_BY_NATURAL_MIN);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> setLimit(long limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be posive (>0)");
        }
        return new DefaultCacheView<K, V>(this, limit);
    }

    /** {@inheritDoc} */
    public <T> CacheView<K, V> orderByAttribute(Attribute<T> attribute, Comparator<? extends T> comparator) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return new DefaultCacheView<K, V>(this, attribute, comparator);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByAttributeMax(Attribute<?> attribute) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        return new DefaultCacheView<K, V>(this, attribute, Comparators.NATURAL_COMPARATOR);
    }

    /** {@inheritDoc} */
    public CacheView<K, V> orderByAttributeMin(Attribute<?> attribute) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        return new DefaultCacheView<K, V>(this, attribute, ViewCommands.ORDER_BY_NATURAL_MIN);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V>[] toArray() {
        return (CacheEntry<K, V>[]) execute(Array.class);
    }
}
