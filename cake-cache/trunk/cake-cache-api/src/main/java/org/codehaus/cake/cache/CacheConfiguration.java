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
package org.codehaus.cake.cache;

import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.service.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.internal.cache.RunAfterCacheStartProcedure;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.annotation.AfterStart;
import org.codehaus.cake.service.common.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.service.common.management.ManagementConfiguration;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;

/**
 * This class is the primary class used for representing the configuration of a {@link Cache}. All general-purpose
 * <tt>Cache</tt> implementation classes should have a constructor with a single argument taking a CacheConfiguration.
 * <p>
 * <b>Usage Examples.</b> <The following creates a new cache with the name <I>MyCache</I>. The cache can hold a
 * maximum of 1000 elements and uses a least-recently-used policy to determine which elements to evict when the
 * specified maximum size has been reached. Finally, the cache and all of its services are registered as a mbean with
 * the {@link java.lang.management.ManagementFactory#getPlatformMBeanServer() platform <tt>MBeanServer</tt>} using the
 * name of the cache. The cache is
 * 
 * <pre>
 * CacheConfiguration&lt;String, Integer&gt; cc = CacheConfiguration.newConfiguration(&quot;MyCache&quot;);
 * cc.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(1000);
 * cc.withManagement().setEnabled(true);
 * Cache&lt;String, Integer&gt; instance = new SynchronizedCache(cc);
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheConfiguration.java 559 2008-01-09 16:28:27Z kasper $
 * @param <K>
 *            the type of keys that should be maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheConfiguration<K, V> extends ContainerConfiguration<Cache> {

    /** Creates a new CacheConfiguration. */
    public CacheConfiguration() {
        addConfiguration(new ExceptionHandlingConfiguration());
        addConfiguration(new CacheAttributeConfiguration());
        addConfiguration(new CacheLoadingConfiguration<K, V>());
        addConfiguration(new ManagementConfiguration());
        addConfiguration(new MemoryStoreConfiguration<K, V>());
        // addConfiguration(new CacheStoreConfiguration());
    }

    // We override a number of setter methods from ContainerConfiguration to
    // allow them to return CacheConfiguration<K, V> instead

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> addToLifecycle(Object o) {
        super.addToLifecycle(o);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public <S> CacheConfiguration<K, V> addToLifecycleAndExport(Class<? extends S> key, S service) {
        super.addToLifecycleAndExport(key, service);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public <S> CacheConfiguration<K, V> addToLifecycleAndExport(Class<? extends S> key, ServiceFactory<S> factory) {
        super.addToLifecycleAndExport(key, factory);
        return this;
    }

    /**
     * Creates a new Cache of the type set using {@link #setType(Class)} from this configuration.
     * 
     * @return the newly created Cache
     * @throws IllegalArgumentException
     *             if a cache of the specified type could not be created
     * @throws IllegalStateException
     *             if no cache type has been set using {@link #setType(Class)}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Cache<K, V> newInstance() {
        return super.newInstance();
    }

    /**
     * Runs the specified procedure after the cache has started. This is basically equivalent to
     * 
     * <pre>
     * CacheConfiguration.addToLifecycle( new Object() {
     *   &#064;AfterStart
     *   public void runAfterStart(Cache&lt;K, V&gt; cache) {
     *     afterStartProcedure.op(cache); //runs the procedure
     *   }
     * }};
     * </pre>
     * 
     * @param runAfterStartProcedure
     *            the procedure to run
     * @return this configuration
     */
    public CacheConfiguration<K, V> runAfterStart(Procedure<Cache<K, V>> runAfterStartProcedure) {
        super.addToLifecycle(new RunAfterCacheStartProcedure<K, V>(runAfterStartProcedure));
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> setClock(Clock clock) {
        super.setClock(clock);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> setDefaultLogger(Logger logger) {
        super.setDefaultLogger(logger);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> setName(String name) {
        super.setName(name);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> setProperty(String key, String value) {
        super.setProperty(key, value);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> setType(Class<? extends Cache> type) {
        super.setType(type);
        return this;
    }

    /**
     * Returns a configuration object that can be used to register which attributes the cache keep count of.
     * 
     * @return a CacheAttributeConfiguration
     */
    public CacheAttributeConfiguration withAttributes() {
        return getConfigurationOfType(CacheAttributeConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how exceptions are handled within the cache
     * 
     * @return an ExceptionHandlingConfiguration
     */
    @SuppressWarnings("unchecked")
    public ExceptionHandlingConfiguration<CacheExceptionHandler<K, V>> withExceptionHandling() {
        return getConfigurationOfType(ExceptionHandlingConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how loading is done in the cache.
     * 
     * @return a CacheLoadingConfiguration
     */
    @SuppressWarnings("unchecked")
    public CacheLoadingConfiguration<K, V> withLoading() {
        return getConfigurationOfType(CacheLoadingConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how services are remotely managed.
     * 
     * @return a ManagementConfiguration
     */
    public ManagementConfiguration withManagement() {
        return getConfigurationOfType(ManagementConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how cache entries are stored in memory.
     * 
     * @return a MemoryStoreConfiguration
     */
    @SuppressWarnings("unchecked")
    public MemoryStoreConfiguration<K, V> withMemoryStore() {
        return getConfigurationOfType(MemoryStoreConfiguration.class);
    }

    /**
     * Creates a new CacheConfiguration with default settings.
     * 
     * @return a new CacheConfiguration with default settings
     * @param <K>
     *            the type of keys that should be maintained by the cache
     * @param <V>
     *            the type of mapped values
     */
    public static <K, V> CacheConfiguration<K, V> newConfiguration() {
        return new CacheConfiguration<K, V>();
    }

    /**
     * Creates a new CacheConfiguration with default settings and the specified name as the name of the cache.
     * 
     * @param name
     *            the name of the cache
     * @return a new CacheConfiguration configured with the specified name
     * @param <K>
     *            the type of keys that should be maintained by the cache
     * @param <V>
     *            the type of mapped values
     */
    public static <K, V> CacheConfiguration<K, V> newConfiguration(String name) {
        CacheConfiguration<K, V> conf = new CacheConfiguration<K, V>();
        conf.setName(name);
        return conf;
    }
}
