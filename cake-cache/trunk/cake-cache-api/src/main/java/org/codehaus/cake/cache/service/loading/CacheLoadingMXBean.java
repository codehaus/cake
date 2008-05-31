/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.loading;

/**
 * The management interface for the loading service.
 * <p>
 * This managed bean is only available at runtime if a cache loader has been set using
 * {@link CacheLoadingConfiguration#setLoader(SimpleCacheLoader)} or
 * {@link CacheLoadingConfiguration#setLoader(org.codehaus.cake.ops.Ops.Op)} and
 * {@link MapManagementConfiguration#setEnabled(boolean)} has been set to <code>true</code>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLoadingMXBean.java 415 2007-11-09 08:25:23Z kasper $
 */
public interface CacheLoadingMXBean {

    /**
     * Attempts to reload all entries that are currently held in the cache.
     */
    void forceLoadAll();

    /**
     * Attempts to reload all entries that needs reloading as decided by
     * {@link CacheLoadingConfiguration#getNeedsReloadFilter()}. If no filter has been set, calls to this method is
     * ignored.
     */
    void loadAll();
}
