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
        Comparator mappedComparator = Comparators.mappedComparator(mapper, comparator);
        return new DefaultQuery<K, V, T>(processor, filter, mappedComparator, mapper, limit);
    }

    public <E> Query<E> to(Op<? super T, ? extends E> mapper) {
        Op op = ObjectOps.compoundMapper(this.mapper, mapper);
        return new DefaultQuery<K, V, E>(processor, filter, comparator, op, limit);
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
                m = a.getClass().getMethod(method);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Cannot find public method '" + method + "()' on  object of type=" + c);
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
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Error) {
                    throw (Error) cause;
                } else if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
