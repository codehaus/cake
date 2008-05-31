/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache.service.exceptionhandling;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

/**
 * An exception service available as an internal service at runtime.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No
 * guarantee is made to the compatibility of this class between different
 * releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: InternalCacheExceptionService.java 544 2008-01-05 01:19:03Z
 *          kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface InternalCacheExceptionService<K, V> extends
		InternalExceptionService<Cache<K, V>> {

	V loadFailed(Throwable cause, K key, AttributeMap map);


//  void initialize(Cache<K, V> cache, CacheConfiguration<K, V> conf);

	//
//	void serviceManagerShutdownFailed(Throwable cause, Object lifecycle);
//
//	void startFailed(CacheConfiguration<K, V> configuration, Object service,
//			Throwable cause);
}
