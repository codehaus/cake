/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.service.exceptionhandling;

import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandler;
import org.omg.CORBA.Object;

/**
 * The purpose of this class is to have one central place where all exceptions that arise within a cache or one of its
 * associated services are handled. One implementation of this class might shutdown the cache for any raised exception.
 * This is often usefull in development environments. Another implementation might just log the exception and continue
 * serving other requests. To allow for easily extending this class with new methods at a later time this class is an
 * abstract class instead of an interface.
 * <p>
 * Currently this class only defines one specialized method,
 * {@link #loadingOfValueFailed(ExceptionContext, Object, MutableAttributeMap)} which is invoked 
 * 
 *  
 * In addition to this general {@link #handle(ExceptionContext)} methods there are also a number of <tt>specialized</tt>
 * methods that handle a particular type of failure. The idea is that all common exception points has a corresponding
 * method in CacheExceptionHandler. For example, whenever an exception occurs while loading an element in a cache loader
 * the {@link #loadingLoadValueFailed(CacheExceptionContext, CacheLoader, Object, MutableAttributeMap)} method is called. In
 * addition to the exception that was raised a number of additional information is provided to this method. For example,
 * the key for which the load failed, the cache in which the cache occured as well as other relevant information.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheExceptionHandler<K, V> extends ExceptionHandler<Cache<K, V>> {

    /**
     * Called whenever a CacheLoader fails while trying to load a value.
     * <p>
     * If an implementation of this method throws a {@link RuntimeException} and the cache loader was invoked through a
     * synchronous method, for example, {@link Cache#get(Object)} the exception will be propagated to the callee. If the
     * cache loader was invoked through an asynchronous method, for example, {@link CacheLoadingService#load(Object)}
     * any runtime exception throw from this method will be silently ignored.
     * <p>
     * The default implementation of this methods calls {@link #handle(ExceptionContext)} with the exception context and
     * returns <code>null</code>.
     * 
     * @param context
     *            an ExceptionContext containing the default logger configured for this cache and the cause of the
     *            failure.
     * @param key
     *            the key for which the load failed
     * @param map
     *            a map of attributes used while trying to load
     * @return a value that can be used instead of the value that couldn't be loaded. If <code>null</code> returned no
     *         entry will be added to the cache for the given key
     */
    public V loadingOfValueFailed(ExceptionContext<Cache<K, V>> context, K key, AttributeMap map) {
        handle(context);
        return null;
    }
}
