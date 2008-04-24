package org.codehaus.cake.cache;

import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.service.exceptionhandling.CacheExceptionHandlingConfiguration;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.store.CacheStoreConfiguration;
import org.codehaus.cake.container.ContainerConfiguration;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.management.ManagementConfiguration;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;

/**
 * This class is the primary class used for representing the configuration of a {@link Cache}. All
 * general-purpose <tt>Cache</tt> implementation classes should have a constructor with a single
 * argument taking a CacheConfiguration.
 * <p>
 * <b>Usage Examples.</b> The following creates a new cache with the name <I>MyCache</I>. The
 * cache can hold a maximum of 1000 elements and uses a least-recently-used policy to determine
 * which elements to evict when the specified maximum size has been reached. Finally, the cache and
 * all of its services are registered as a mbean with the
 * {@link java.lang.management.ManagementFactory#getPlatformMBeanServer platform
 * <tt>MBeanServer</tt>} using the name of the cache.
 * 
 * <pre>
 * CacheConfiguration&lt;String, Integer&gt; cc = CacheConfiguration.newConfiguration(&quot;MyCache&quot;);
 * cc.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(1000);
 * cc.withManagement().setEnabled(true);
 * Cache&lt;String, Integer&gt; instance = cc.newInstance(SynchronizedCache.class);
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

    public CacheConfiguration() {
        addConfiguration(new CacheExceptionHandlingConfiguration());
        addConfiguration(new CacheAttributeConfiguration());
        addConfiguration(new CacheLoadingConfiguration());
        addConfiguration(new ManagementConfiguration());
        addConfiguration(new MemoryStoreConfiguration());
        addConfiguration(new ExecutorsConfiguration());
        addConfiguration(new CacheStoreConfiguration());
    }

    @Override
    public CacheConfiguration<K, V> addService(Object o) {
        super.addService(o);
        return this;
    }

    @Override
    public CacheConfiguration<K, V> setClock(Clock clock) {
        super.setClock(clock);
        return this;
    }

    @Override
    public CacheConfiguration<K, V> setDefaultLogger(Logger logger) {
        super.setDefaultLogger(logger);
        return this;
    }

    @Override
    public CacheConfiguration<K, V> setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public CacheConfiguration<K, V> setProperty(String key, String value) {
        super.setProperty(key, value);
        return this;
    }

    @Override
    public CacheConfiguration<K, V> setType(Class<? extends Cache> type) {
        super.setType(type);
        return this;
    }

    public CacheAttributeConfiguration withAttributes() {
        return getConfigurationOfType(CacheAttributeConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how services are remotely managed.
     * 
     * @return a ManagementConfiguration
     */
    public CacheExceptionHandlingConfiguration<K, V> withExceptionHandling() {
        return getConfigurationOfType(CacheExceptionHandlingConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how tasks are executed within a
     * cache.
     * 
     * @return a ExecutorManagerBuilder
     */
    public ExecutorsConfiguration withExecutors() {
        return getConfigurationOfType(ExecutorsConfiguration.class);
    }

    /**
     * Returns a configuration object that can be used to control how loading is done in the cache.
     * 
     * @return a CacheLoadingConfiguration
     */
    public CacheLoadingConfiguration<K, V> withLoading() {
        return getConfigurationOfType(CacheLoadingConfiguration.class);
    }

    public MemoryStoreConfiguration<K, V> withMemoryStore() {
        return getConfigurationOfType(MemoryStoreConfiguration.class);
    }

    public CacheStoreConfiguration<K, V> withStore() {
        return getConfigurationOfType(CacheStoreConfiguration.class);
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
     * Creates a new CacheConfiguration with default settings and the specified name as the name of
     * the cache .
     * 
     * @param name
     *            the name of the cache
     * @return a new CacheConfiguration with the specified name
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
