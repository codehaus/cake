package org.codehaus.cake.internal.cache.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.ops.Ops.BinaryOp;
import org.codehaus.cake.ops.Ops.Op;

public abstract class AbstractCacheLoader<K, V> implements InternalCacheLoader<K, V>{

    private InternalCacheExceptionService<K, V> exceptionHandler;

    public AbstractCacheLoader(InternalCacheExceptionService<K, V> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public V doLoad(SimpleCacheLoader<K, V> loader, K key, AttributeMap attributes) {
        V v = null;
        try {
            v = loader.load(key, attributes);
        } catch (Throwable e) {
            v = exceptionHandler.loadFailed(e, key, attributes);
        }
        return v;
    }

    static <K, V> SimpleCacheLoader<K, V> getSimpleLoader(CacheLoadingConfiguration<K, V> conf) {
        Object loader = conf.getLoader();
        if (loader instanceof Op) {
            return new OpToSimpleLoader((Op) loader);
        } else if (loader instanceof SimpleCacheLoader) {
            return (SimpleCacheLoader) loader;
        } else {
            throw new IllegalArgumentException("Unknown loader type [type =" + loader.getClass()
                    + "]");
        }
    }

    static class OpToSimpleLoader<K, V> implements SimpleCacheLoader<K, V> {
        private final Op<K, V> op;

        public OpToSimpleLoader(Op<K, V> op) {
            this.op = op;
        }

        public V load(K key, AttributeMap attributes) throws Exception {
            return op.op(key);
        }
    }

    static class OpsToSimpleLoader<K, V> implements SimpleCacheLoader<K, V> {
        private final BinaryOp<K, AttributeMap, V> op;

        public OpsToSimpleLoader(BinaryOp<K, AttributeMap, V> op) {
            this.op = op;
        }

        public V load(K key, AttributeMap attributes) throws Exception {
            return op.op(key, attributes);
        }
    }

    public void loadAsync(Map<? extends K, AttributeMap> map) {
        for (Map.Entry<? extends K, AttributeMap> e : map.entrySet()) {
            loadAsync(e.getKey(), e.getValue());
        }
    }
}
