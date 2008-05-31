/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.exceptionhandling;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandler;

/**
 * The purpose of this class is to have one central place where all exceptions that arise
 * within a cache or one of its associated services are handled. One implementation of
 * this class might shutdown the cache for any raised exception. This is often usefull in
 * development environments. Another implementation might just log the exception and
 * continue serving other requests. To allow for easily extending this class with new
 * methods at a later time this class is an abstract class instead of an interface.
 * <p>
 * There are 4 basis <tt>general</tt> methods for handling failures occuring in the
 * cache.
 * <ul>
 * <li>{@link #op(CacheExceptionContext)} which is called, on a best effort basis,
 * whenever an Error is raised within the cache. No reasonable application should not try
 * to handle this, except for writing as much debug information as possible.
 * <li>{@link #handleException(CacheExceptionContext, Exception)} which is called
 * whenever a condition arises that a reasonable application might want to handle. For
 * example, if a {@link CacheLoader} fails to load a value for some specified key. In most
 * situations these should just be logged and the cache should continue as nothing has
 * happend.
 * <li>{@link #handleRuntimeException(CacheExceptionContext, RuntimeException)} which is
 * called when a programmatic error arises from which an application cannot normally
 * recover. This could, for example, be some user provided callback that fails in some
 * mysterious way. Or even worse that the cache implementation contains a bug. Of course,
 * this is highly unlikely if using one of the default implementation provided by Cake
 * Cache;).
 * <li>{@link #warning(CacheExceptionContext)} which is called whenever a some kind of
 * inconsistency arrises in the system. Normally this always indicates a non-critical
 * problem that should be fixed at some time. For example, if a CacheLoader tries to set
 * the creation time of a newly loaded element to a negative value.
 * </ul>
 * <p>
 * In addition to this general methods there are also a number of <tt>specialized</tt>
 * methods that handle a particular type of failure. The idea is that all common exception
 * points has a corresponding method in CacheExceptionHandler. For example, whenever an
 * exception occurs while loading an element in a cache loader the
 * {@link #loadingLoadValueFailed(CacheExceptionContext, CacheLoader, Object, AttributeMap)}
 * method is called. In addition to the exception that was raised a number of additional
 * information is provided to this method. For example, the key for which the load failed,
 * the cache in which the cache occured as well as other relevant information.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheExceptionHandler.java 538 2007-12-31 00:18:13Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheExceptionHandler<K, V> extends ExceptionHandler<Cache<K, V>> {

    /**
     * Called whenever a CacheLoader fails while trying to load a value.
     * <p>
     * If this method chooses to throw a {@link RuntimeException} and the cache loader was
     * invoked through a synchronous method, for example, {@link Cache#get(Object)} the
     * exception will be propagated to the callee. If the cache loader was invoked through
     * an asynchronous method, for example, {@link CacheLoadingService#load(Object)} any
     * exception throw from this method will not be visible to the user.
     * <p>
     * The default implementation, will just log any exception.
     * 
     * @param context
     *            an CacheExceptionContext containing the default logger configured for
     *            this cache and the cause of the failure. The exception message includes
     *            the
     * @param loader
     *            the cacheloader that failed to load a value
     * @param key
     *            the key for which the load failed
     * @param map
     *            a map of attributes used while trying to load
     * @return a value that can be used instead of the value that couldn't be loaded. If
     *         <code>null</code> returned no entry will be added to the cache for the
     *         given key
     */
    public V loadingOfValueFailed(ExceptionContext<Cache<K, V>> context, K key, AttributeMap map) {
        handle(context);
        return null;
    }

}
