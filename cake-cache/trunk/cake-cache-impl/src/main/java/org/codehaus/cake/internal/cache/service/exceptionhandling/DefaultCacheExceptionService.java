/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.cache.service.exceptionhandling;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.AbstractExceptionService;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.spi.ExceptionContext;
import org.codehaus.cake.service.spi.ExceptionHandlingConfiguration;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

/**
 * An exception service available as an internal service at runtime.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class DefaultCacheExceptionService<K, V> extends AbstractExceptionService<Cache<K, V>> implements
        InternalCacheExceptionService<K, V> {
    /** The CacheExceptionHandler configured for this cache. */
    private CacheExceptionHandler<K, V> exceptionHandler;

    public DefaultCacheExceptionService(Container container, Composer composer,
            ContainerConfiguration containerConfiguration,
            ExceptionHandlingConfiguration<CacheExceptionHandler<K, V>> configuration) {
        super(container, composer, containerConfiguration, configuration.getExceptionLogger());
        CacheExceptionHandler<K, V> exceptionHandler = configuration.getExceptionHandler();
        this.exceptionHandler = exceptionHandler == null ? new CacheExceptionHandler<K, V>() : exceptionHandler;
    }

    /** {@inheritDoc} */
    @Override
    protected void handle(ExceptionContext<Cache<K, V>> context) {
        exceptionHandler.handle(context);
    }

    /** {@inheritDoc} */
    public V loadFailed(Throwable cause, K key, MutableAttributeMap map) {
        String message = "Loading failed [key = " + key + "]";
        return exceptionHandler.loadingOfValueFailed(createContext(cause, message, Logger.Level.Error), key, map);
    }
}
