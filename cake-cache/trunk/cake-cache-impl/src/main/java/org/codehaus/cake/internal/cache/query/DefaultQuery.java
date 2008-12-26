package org.codehaus.cake.internal.cache.query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.query.Query;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultQuery<K, V, T> implements Query<T> {

    private final CacheProcessor<K, V> processor;
    private final Predicate<CacheEntry<K, V>> filter;
    private final Comparator<CacheEntry<K, V>> comparator;
    private final Op<CacheEntry<K, V>, T> mapper;
    private final int limit;

    public DefaultQuery(CacheProcessor<K, V> processor, Predicate<CacheEntry<K, V>> filter,
            Comparator<CacheEntry<K, V>> comparator, Op<CacheEntry<K, V>, T> mapper, int limit) {
        this.processor = processor;
        this.filter = filter;
        this.comparator = comparator;
        this.mapper = mapper;
        this.limit = limit;
    }

    public Iterator<T> iterator() {
        return asList().iterator();
    }

    public Query<T> setLimit(int maxresults) {
        if (maxresults <= 0) {
            throw new IllegalArgumentException("maxresults must be posive (>0)");
        }
        return new DefaultQuery<K, V, T>(processor, filter, comparator, mapper, maxresults);
    }

    public List<T> asList() {
        return processor.process(filter, comparator, mapper, limit);
    }

    public Query<T> orderBy(Comparator<? super T> comparator) {
        return new DefaultQuery<K, V, T>(processor, filter,(Comparator) Comparators.mappedComparator(mapper, comparator), mapper,
                limit);
    }

    public <E> Query<E> to(Op<T, E> transformer) {
        return new DefaultQuery<K, V, E>(processor, filter, comparator, (Op)  ObjectOps.compoundMapper(mapper, transformer),
                limit);
    }
    @SuppressWarnings("unchecked")
    public <E> Query<E> to(String method, Class<E> to) {
        return to(new ReflectionMapper<T, E>(method, to));
    }

    static class ReflectionMapper<F, R> implements Op<F, R> {
        private final String method;
        private final Class<R> result;

        private Method cached;
        private Class<?> cachedArgument;

        ReflectionMapper(String method, Class<R> result) {
            this.method = method;
            this.result = result;
        }

        public R op(F a) {
            Method cached = this.cached;
            Class c = a.getClass();
            if (cached != null && cachedArgument == c) {
                return invoke(cached, a);
            }
            final Method m;
            try {
                m = a.getClass().getMethod(method, null);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Object of type" + c + " does not have method " + method);
            }
            // Check result type;
            cached = m;
            cachedArgument = c;
            return invoke(m, a);
        }

        private R invoke(Method m, Object target) {
            try {
                Object result = m.invoke(target, (Object[]) null);
                return (R) result;
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
