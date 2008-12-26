package org.codehaus.cake.internal.cache.query;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.query.MapQuery;
import org.codehaus.cake.cache.query.Query;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.util.CollectionUtils.SimpleImmutableEntry;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultMapQuery<K, V, K1, V1> extends AbstractMapQuery<K1, V1> {

    private final CacheProcessor<K, V> processor;
    private final Predicate<CacheEntry<K, V>> filter;
    private final Comparator<CacheEntry<K, V>> comparator;
    private final Op<CacheEntry<K, V>, CacheEntry<K1, V1>> mapper;
    private final int limit;

    public DefaultMapQuery(CacheProcessor<K, V> processor, Predicate<CacheEntry<K, V>> filter,
            Comparator<CacheEntry<K, V>> comparator, Op<CacheEntry<K, V>, CacheEntry<K1, V1>> mapper, int limit) {
        this.processor = processor;
        this.filter = filter;
        this.comparator = comparator;
        this.mapper = mapper;
        this.limit = limit;
    }

    public MapQuery<K1, V1> setLimit(int maxresults) {
        if (maxresults <= 0) {
            throw new IllegalArgumentException("maxresults must be posive (>0)");
        }
        return new DefaultMapQuery<K, V, K1, V1>(processor, filter, comparator, mapper, maxresults);
    }

    public Query<K1> keys() {
        return new DefaultQuery<K, V, K1>(processor, filter, comparator,
                mapper == null ? CollectionOps.MAP_ENTRY_TO_KEY_OP : ObjectOps.compoundMapper(mapper,
                        CollectionOps.MAP_ENTRY_TO_KEY_OP), limit);
    }

    public Query<V1> values() {
        return new DefaultQuery<K, V, V1>(processor, filter, comparator,
                mapper == null ? CollectionOps.MAP_ENTRY_TO_VALUE_OP : ObjectOps.compoundMapper(mapper,
                        CollectionOps.MAP_ENTRY_TO_VALUE_OP), limit);
    }

    public Map<K1, V1> asMap() {
        LinkedHashMap<K1, V1> map = new LinkedHashMap<K1, V1>();
        for (Map.Entry<K1, V1> e : this) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    public <E> MapQuery<E, V1> keyTo(Op<K1, E> transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is null");
        }
        Op<Map.Entry<K1, V1>, Map.Entry<E, V1>> op = new MapKey<K1, V1, E>(transformer);
        return new DefaultMapQuery<K, V, E, V1>(processor, filter, comparator, mapper == null ? (Op) op : ObjectOps
                .compoundMapper(mapper, op), limit);
    }

    public MapQuery<K1, V1> orderBy(Comparator<? super Entry<K1, V1>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        final Comparator<CacheEntry<K, V>> c;
        if (mapper == null) {
            c = this.comparator == null ? (Comparator) comparator : Comparators.compoundComparator(this.comparator,
                    (Comparator) comparator);

        } else {
            c = this.comparator == null ? Comparators.mappedComparator(mapper, comparator) : (Comparator) Comparators
                    .compoundComparator(this.comparator, Comparators.mappedComparator(mapper, comparator));
        }
        return new DefaultMapQuery<K, V, K1, V1>(processor, filter, c, mapper, limit);
    }

    public <E> MapQuery<K1, E> valueTo(Op<V1, E> transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is null");
        }
        Op<Map.Entry<K1, V1>, Map.Entry<K1, E>> op = new MapValue<K1, V1, E>(transformer);
        return new DefaultMapQuery<K, V, K1, E>(processor, filter, comparator, mapper == null ? (Op) op : ObjectOps
                .compoundMapper(mapper, op), limit);
    }

    public List<Entry<K1, V1>> asList() {
       return (List) processor.process(filter, comparator, mapper, limit);
    }

    public <E> Query<E> to(Op<Entry<K1, V1>, E> transformer) {
        Op<CacheEntry<K, V>, E> op = mapper == null ? (Op) transformer : ObjectOps.compoundMapper(mapper, transformer);
        return new DefaultQuery<K, V, E>(processor, filter, comparator, op, limit);
    }

    static class MapKey<K, V, K1> implements Op<Map.Entry<K, V>, Map.Entry<K1, V>> {
        private final Op<K, K1> op;

        MapKey(Op<K, K1> op) {
            this.op = op;
        }

        public Entry<K1, V> op(Entry<K, V> a) {
            K key = a.getKey();
            K1 mapped = op.op(key);
            return new SimpleImmutableEntry<K1, V>(mapped, a.getValue());
        }
    }

    static class MapValue<K, V, V1> implements Op<Map.Entry<K, V>, Map.Entry<K, V1>> {
        private final Op<V, V1> op;

        MapValue(Op<V, V1> op) {
            this.op = op;
        }

        public Entry<K, V1> op(Entry<K, V> a) {
            V value = a.getValue();
            V1 mapped = op.op(value);
            return new SimpleImmutableEntry<K, V1>(a.getKey(), mapped);
        }
    }
}
