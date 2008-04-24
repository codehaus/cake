/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.CacheEntry;

public interface InternalCacheEntry<K, V> extends CacheEntry<K, V> {
    CacheEntry<K, V> safe();
    boolean isDead();
}
