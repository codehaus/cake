package org.codehaus.cake.internal.cache.service.exceptionhandling;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.cache.exceptionhandling.CacheExceptionHandlingConfiguration;
import org.codehaus.cake.internal.service.exceptionhandling.AbstractExceptionService;
import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;

public class DefaultCacheExceptionService<K, V> extends AbstractExceptionService<Cache<K, V>> implements
        InternalCacheExceptionService<K, V> {
    /** The CacheExceptionHandler configured for this cache. */
    private CacheExceptionHandler<K, V> exceptionHandler;

    public DefaultCacheExceptionService(ContainerInfo info, ContainerConfiguration<Cache<K, V>> containerConfiguration,
            CacheExceptionHandlingConfiguration<K, V> configuration) {
        super(info, containerConfiguration, configuration.getExceptionLogger());
        CacheExceptionHandler<K, V> exceptionHandler = configuration.getExceptionHandler();
        this.exceptionHandler = exceptionHandler == null ? new CacheExceptionHandler<K, V>() : exceptionHandler;
    }

    @Override
    protected void handle(ExceptionContext<Cache<K, V>> context) {
        exceptionHandler.handle(context);
    }

    public void initialize(Cache<K, V> cache, CacheConfiguration<K, V> conf) {
    // TODO Auto-generated method stub
    }

    public V loadFailed(Throwable cause, K key, AttributeMap map) {
        fatal("", cause);
        // TODO Auto-generated method stub
        return null;
    }

    public void serviceManagerShutdownFailed(Throwable cause, Object lifecycle) {
        fatal("", cause);

    }

    public void startFailed(CacheConfiguration<K, V> configuration, Object service, Throwable cause) {
        fatal("", cause);
    }

    public void terminated() {}
}
