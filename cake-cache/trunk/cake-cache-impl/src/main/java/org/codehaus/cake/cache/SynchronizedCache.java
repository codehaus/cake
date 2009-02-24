package org.codehaus.cake.cache;

import java.util.concurrent.ExecutorService;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.memorystore.ExportedSynchronizedMemoryStoreService;
import org.codehaus.cake.internal.cache.processor.DefaultCacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.SynchronizedCacheProcessor;
import org.codehaus.cake.internal.cache.service.attribute.MemorySparseAttributeService;
import org.codehaus.cake.internal.cache.service.loading.DefaultCacheLoadingService;
import org.codehaus.cake.internal.cache.service.loading.ThreadSafeCacheLoader;
import org.codehaus.cake.internal.cache.service.memorystore.views.SynchronizedCollectionViewFactory;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.internal.service.configuration.SynchronizedConfigurationService;
import org.codehaus.cake.internal.service.executor.DefaultExecutorService;
import org.codehaus.cake.internal.service.executor.DefaultForkJoinPool;
import org.codehaus.cake.internal.service.executor.DefaultScheduledExecutorService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.Container;

/**
 * A <tt>synchronized</tt> {@link Cache} implementation.
 * <p>
 * It is imperative that the user manually synchronize on the cache when iterating over any of its collection views:
 * 
 * <pre>
 *  Cache c = new SynchronizedCache();
 *      ...
 *  Set s = c.keySet();  // Needn't be in synchronized block
 *      ...
 *  synchronized(c) {  // Synchronizing on c, not s!
 *      Iterator i = s.iterator(); // Must be in synchronized block
 *      while (i.hasNext())
 *          foo(i.next());
 *  }
 * </pre>
 * 
 * Failure to follow this advice may result in non-deterministic behavior. Users must also make sure that when iterating
 * over any of the the collections views through calls to {@link #filter()} synchronization must be done on the
 * originally cache.
 * 
 * <pre>
 *  Cache&lt;Number, String&gt; c = new SynchronizedCache&lt;Number, String&gt;();
 *  Cache&lt;Integer,String&gt; filtered = c.select.onKeyType(Integer.class)
 *      ...
 *  Set&lt;Integer&gt; s = filtered.keySet();  // Needn't be in synchronized block
 *      ...
 *  synchronized(c) {  // Synchronizing on c, not filtered or s!
 *      Iterator&lt;Integer&gt; i = s.iterator(); // Must be in synchronized block
 *      while (i.hasNext())
 *          foo(i.next());
 *  }
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
@Container.SupportedServices( { ExecutorService.class, MemoryStoreService.class, CacheLoadingService.class,
        Manageable.class })
public class SynchronizedCache<K, V> extends AbstractCache<K, V> {

    private final Object mutex;

    /** Creates a new SynchronizedCache with default configuration. */
    public SynchronizedCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }
    /** Creates a new SynchronizedCache with default configuration. */
    public SynchronizedCache(Container parent, CacheConfiguration<K, V> configuration) {
        this(CacheConfiguration.<K, V> newConfiguration());
    }
    /**
     * Creates a new SynchronizedCache with the specified configuration.
     * 
     * @param configuration
     *            the configuration
     */
    public SynchronizedCache(CacheConfiguration<K, V> configuration) {
        super(createComposer(configuration));
        this.mutex = this;
    }

    SynchronizedCache(SynchronizedCache<K, V> parent, Predicate<CacheEntry<K, V>> filter) {
        super(parent, filter);
        mutex = parent.mutex;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(Object key) {
        synchronized (mutex) {
            return super.containsKey(key);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(Object value) {
        synchronized (mutex) {
            return super.containsValue(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public V peek(K key) {
        synchronized (mutex) {
            return super.peek(key);
        }
    }

    /** {@inheritDoc} */
    @Override
    public CacheEntry<K, V> peekEntry(K key) {
        synchronized (mutex) {
            return super.peekEntry(key);
        }
    }

    /** {@inheritDoc} */
    public CacheSelector<K, V> filter() {
        return new AbstractCacheSelector<K, V>() {
            public Cache<K, V> on(Predicate<CacheEntry<K, V>> filter) {
                if (filter == null) {
                    throw new NullPointerException("filter is null");
                }
                return new SynchronizedCache<K, V>(SynchronizedCache.this, filter);
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        synchronized (mutex) {
            return super.size();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        synchronized (mutex) {
            return super.toString();
        }
    }

    private static Composer createComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = newComposer(configuration);

        composer.registerImplementation(ExportedSynchronizedMemoryStoreService.class);

        composer.registerImplementation(SynchronizedConfigurationService.class);
        composer.registerImplementation(DefaultCacheRequestFactory.class);
        composer.registerImplementation(SynchronizedCacheProcessor.class);

        // Common components
        composer.registerImplementation(UnsynchronizedRunState.class);
        if (configuration.withManagement().isEnabled()) {
            composer.registerImplementation(DefaultManagementService.class);
        }

        // Cache components
        composer.registerImplementation(SynchronizedCollectionViewFactory.class);
        composer.registerImplementation(DefaultExecutorService.class);
        composer.registerImplementation(DefaultScheduledExecutorService.class);
        composer.registerImplementation(DefaultForkJoinPool.class);

        composer.registerImplementation(MemorySparseAttributeService.class);
        if (configuration.withLoading().getLoader() != null) {
            composer.registerImplementation(ThreadSafeCacheLoader.class);
            composer.registerImplementation(DefaultCacheLoadingService.class);
        }

        return composer;
    }

    /**
     * Creates a new SynchronizedCache from the specified configuration.
     * 
     * @param configuration
     *            the cache configuration to create the cache from
     * @param <K>
     *            the types of key maintained by the cache
     * @param <V>
     *            the types of values maintained by the cache
     * @return a new SynchronizedCache
     */
    public static <K, V> SynchronizedCache<K, V> from(CacheConfiguration<K, V> configuration) {
        return new SynchronizedCache<K, V>(configuration);
    }
}
