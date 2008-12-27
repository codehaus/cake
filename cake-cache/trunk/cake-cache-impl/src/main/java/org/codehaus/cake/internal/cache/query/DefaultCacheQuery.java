package org.codehaus.cake.internal.cache.query;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.cache.query.CacheQuery;
import org.codehaus.cake.cache.query.MapQuery;
import org.codehaus.cake.cache.query.Query;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.util.CollectionUtils.SimpleImmutableEntry;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultCacheQuery<K, V, K1, V1> extends AbstractCacheQuery<K1, V1> {

    private final Comparator<CacheEntry<K, V>> comparator;

    private final Predicate<CacheEntry<K, V>> filter;

    private final int limit;
    private final Op<CacheEntry<K, V>, CacheEntry<K1, V1>> mapper;
    private final CacheProcessor<K, V> processor;

    public DefaultCacheQuery(CacheProcessor<K, V> processor, Predicate<CacheEntry<K, V>> filter) {
        this(processor, filter, null, null, Integer.MAX_VALUE);
    }

    public DefaultCacheQuery(CacheProcessor<K, V> processor, Predicate<CacheEntry<K, V>> filter,
            Comparator<CacheEntry<K, V>> comparator, Op<CacheEntry<K, V>, CacheEntry<K1, V1>> mapper, int limit) {
        this.limit = limit;
        this.processor = processor;
        this.filter = filter;
        this.comparator = comparator;
        this.mapper = mapper;
    }

    public List<CacheEntry<K1, V1>> asList() {
        return (List) processor.process(filter, comparator, mapper, limit);
    }

    public <T> Query<T> attribute(Attribute<T> attribute) {
        Op op = new ExtractAttribute<T>(attribute);
        return new DefaultQuery<K, V, T>(processor, filter, comparator, mapper == null ? op : ObjectOps.compoundMapper(
                mapper, op), limit);
    }

    public Query<K1> keys() {
        return new DefaultQuery<K, V, K1>(processor, filter, comparator,
                mapper == null ? CollectionOps.MAP_ENTRY_TO_KEY_OP : ObjectOps.compoundMapper(mapper,
                        CollectionOps.MAP_ENTRY_TO_KEY_OP), limit);
    }

    public <E> CacheQuery<E, V1> keyTo(Op<? super K1, ? extends E> transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is null");
        }
        Op<CacheEntry<K1, V1>, CacheEntry<E, V1>> op = new MapKey<K1, V1, E>(transformer);
        return new DefaultCacheQuery<K, V, E, V1>(processor, filter, comparator, mapper == null ? (Op) op : ObjectOps
                .compoundMapper(mapper, op), limit);
    }

    public MapQuery<K1, V1> map() {
        return new DefaultMapQuery<K, V, K1, V1>(processor, filter, comparator, mapper, limit);
    }

    public <T> MapQuery<K1, T> map(Attribute<T> attribute) {
        Op<CacheEntry<K1, ?>, Map.Entry<K1, T>> op = new MapAttribute<K1, T>(attribute);
        return new DefaultMapQuery<K, V, K1, T>(processor, filter, comparator, mapper == null ? op : ObjectOps
                .compoundMapper(mapper, (Op) op), limit);
    }

    public CacheQuery<K1, V1> orderBy(Comparator<? super CacheEntry<K1, V1>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        final Comparator<CacheEntry<K, V>> c;
        if (mapper == null) {
            c = this.comparator == null ? (Comparator) comparator : Comparators.compoundComparator(this.comparator,
                    (Comparator) comparator);

        } else {
            c = this.comparator == null ? Comparators.mappedComparator(mapper, comparator) :  (Comparator)Comparators
                    .compoundComparator(this.comparator, Comparators.mappedComparator(mapper, comparator));
        }
        return new DefaultCacheQuery<K, V, K1, V1>(processor, filter, c, mapper, limit);
    }

    public void putInto(Cache<K1, V1> cache) {
        CrudWriter<K1, V1, Void> cw = cache.withCrud().write();
        for (CacheEntry<K1, V1> e : asList()) {
            CacheEntry<K1, V1> conv = e;
            cw.put(conv.getKey(), conv.getValue(), conv);
        }
    }

    public CacheQuery<K1, V1> setLimit(int maxresults) {
        if (maxresults <= 0) {
            throw new IllegalArgumentException("maxresults must be posive (>0)");
        }
        return new DefaultCacheQuery<K, V, K1, V1>(processor, filter, comparator, mapper, maxresults);
    }

    public <E> Query<E> to(Op<CacheEntry<K1, V1>, E> transformer) {
        Op<CacheEntry<K, V>, E> op = mapper == null ? (Op) transformer : ObjectOps.compoundMapper(mapper, transformer);
        return new DefaultQuery<K, V, E>(processor, filter, comparator, op, limit);
    }

    public Query<V1> values() {
        return new DefaultQuery<K, V, V1>(processor, filter, comparator,
                mapper == null ? CollectionOps.MAP_ENTRY_TO_VALUE_OP : ObjectOps.compoundMapper(mapper,
                        CollectionOps.MAP_ENTRY_TO_VALUE_OP), limit);
    }

    public <E> CacheQuery<K1, E> valueTo(Op<? super V1, ? extends E> transformer) {
        if (transformer == null) {
            throw new NullPointerException("transformer is null");
        }
        Op<CacheEntry<K1, V1>, CacheEntry<K1, E>> op = new MapValue<K1, V1, E>(transformer);
        return new DefaultCacheQuery<K, V, K1, E>(processor, filter, comparator, mapper == null ? (Op) op : ObjectOps
                .compoundMapper(mapper, op), limit);
    }

    static class MapKey<K, V, K1> implements Op<CacheEntry<K, V>, CacheEntry<K1, V>> {
        private final Op<? super K, ? extends K1> op;

        MapKey(Op<? super K, ? extends K1> op) {
            this.op = op;
        }

        public CacheEntry<K1, V> op(CacheEntry<K, V> a) {
            K key = a.getKey();
            K1 mapped = op.op(key);
            return Caches.newEntry(mapped, a.getValue(), a);
        }
    }

    static class MapValue<K, V, V1> implements Op<CacheEntry<K, V>, CacheEntry<K, V1>> {
        private final Op<? super V, ? extends V1> op;

        MapValue(Op<? super V, ? extends V1> op) {
            this.op = op;
        }

        public CacheEntry<K, V1> op(CacheEntry<K, V> a) {
            V value = a.getValue();
            V1 mapped = op.op(value);
            return Caches.newEntry(a.getKey(), mapped, a);
        }
    }

    static class MapAttribute<K, T> implements Op<CacheEntry<K, ?>, Map.Entry<K, T>> {
        private final Attribute<T> attribute;

        MapAttribute(Attribute<T> attribute) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
        }

        public Map.Entry<K, T> op(CacheEntry<K, ?> a) {
            T value = a.get(attribute);
            return new SimpleImmutableEntry<K, T>(a.getKey(), value);
        }
    }
}
