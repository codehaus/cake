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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.codehaus.cake.cache.service.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.internal.cache.RunAfterCacheStartProcedure;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.ops.Ops.Procedure;

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
 * @version $Id$
 * @param <K>
 *            the type of keys that should be maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheConfiguration<K, V> extends ContainerConfiguration {

    /** The attributes that can be attached to each cache entry. */
    private LinkedHashSet<Attribute<?>> entryAttributes = new LinkedHashSet<Attribute<?>>();

    /** Creates a new CacheConfiguration. */
    public CacheConfiguration() {
        addConfiguration(new ExceptionHandlingConfiguration<CacheExceptionHandler<K, V>>());
        addConfiguration(new CacheLoadingConfiguration<K, V>());
        addConfiguration(new ManagementConfiguration());
        addConfiguration(new MemoryStoreConfiguration<K, V>());
    }

    /**
     * Register an attribute(s) that should be attach to each {@link CacheEntry}. The attributes can be some of the
     * attributes defined in {@link CacheEntry} which are handled specially by the cache. For example, the
     * {@link CacheEntry#TIME_CREATED} attribute which will make sure the cache records the insertion time of all
     * entries. The following example, shows how to configure the cache to use this attribute, and how to get a hold of
     * the creation time of the entry
     * 
     * <pre>
     * CacheConfiguration&lt;Integer, String&gt; conf = CacheConfiguration.newConfiguration();
     * conf.withAttributes().add(CacheEntry.TIME_CREATED);
     * Cache&lt;Integer, String&gt; cache = SynchronizedCache.from(conf);
     * // inserting an entry and getting hold of the creatation time
     * cache.put(1, &quot;hello&quot;);
     * CacheEntry&lt;Integer, String&gt; e = cache.getEntry(1);
     * System.out.println(CacheEntry.TIME_CREATED.get(e));
     * </pre>
     * 
     * Or they can be custom-defined attributes that only have a meaning when interpreted by the user of the cache.
     * 
     * 
     * @param a
     *            the attribute(s) to add
     * @return this configuration
     */
    public CacheConfiguration<K, V> addEntryAttributes(Attribute<?>... a) {
        for (Attribute<?> aa : a) {
            if (entryAttributes.contains(aa)) {
                throw new IllegalArgumentException("Attribute has already been added [Attribute =" + aa + "");
            }
        }
        for (Attribute<?> aa : a) {
            entryAttributes.add(aa);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public <S> CacheConfiguration<K, V> addService(Class<? extends S> key, S service) {
        super.addService(key, service);
        return this;
    }

    // We override a number of setter methods from ContainerConfiguration to
    // allow them to return CacheConfiguration<K, V> instead

    /** {@inheritDoc} */
    @Override
    public <S> CacheConfiguration<K, V> addService(Class<? extends S> key, ServiceProvider<S> factory) {
        super.addService(key, factory);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CacheConfiguration<K, V> addService(Object o) {
        super.addService(o);
        return this;
    }

    /** @return a list of all the attributes that has been added through calls to {@link #add(Attribute...)} */
    public List<Attribute<?>> getAllEntryAttributes() {
        return new ArrayList<Attribute<?>>(entryAttributes);
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
        super.addService(new RunAfterCacheStartProcedure<K, V>(runAfterStartProcedure));
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
