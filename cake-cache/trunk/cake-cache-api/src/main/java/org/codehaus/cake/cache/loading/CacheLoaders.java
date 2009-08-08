package org.codehaus.cake.cache.loading;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.Procedure;

public class CacheLoaders {

    /** Cannot instantiate. */
    private CacheLoaders() {
    }

    /**
     * Equivalent to {@link #scheduleForcedPartialReload(Cache, ScheduledExecutorService, int, long, TimeUnit)} except
     * that the caches default scheduler will be used (<tt>cache.with().scheduledExecutor()).
     * 
     * @param cache
     * @param parts
     * @param period
     * @param TimeUnit
     *            the time unit of the period parameter
     */
    public static ScheduledFuture<?> schedulePartialReload(Cache<?, ?> cache, boolean isForced, int parts, long period,
            TimeUnit unit) {
        return schedulePartialReload(cache, isForced, cache.with().scheduledExecutor(), parts, period, unit);
    }

    /**
     * Equivalent to {@link #scheduleForcedPartialReload(Cache, int, long, TimeUnit)} except that the a seperate
     * {@link ScheduledExecutorService} can be used instead of the caches default scheduled executor service.
     * 
     * @param cache
     *            the cache where entries should be reloaded
     * @param ses
     *            the scheduled executor to register the
     * @param parts
     * @param period
     * @param TimeUnit
     *            the time unit of the period parameter
     */
    public static ScheduledFuture<?> schedulePartialReload(Cache<?, ?> cache, boolean isForced,
            ScheduledExecutorService ses, int parts, long period, TimeUnit unit) {
        return ses.scheduleAtFixedRate(new ModuloRefreshCache(cache, isForced, parts), period, period, unit);
    }

    /**
     * Registers a service that will reload
     * 
     * <p>
     * Technically this works
     * <p>
     * If the cache has already been instantiated {@link #scheduleForcedPartialReload(Cache, int, long, TimeUnit)} can
     * be used instead.
     * 
     * @param configuration
     *            the cache configuration
     * @param parts
     * @param period
     * @param TimeUnit
     *            the time unit of the period parameter
     */
    @SuppressWarnings("unchecked")
    public static void schedulePartialReload(CacheConfiguration<?, ?> configuration, final boolean isForced,
            final int parts, final long period, final TimeUnit unit) {
        // TODO verify parameters, since we dispatch async
        configuration.runAfterStart((Procedure) new Procedure<Cache<?, ?>>() {
            public void op(Cache<?, ?> cache) {
                schedulePartialReload(cache, isForced, parts, period, unit);
            }
        });
    }

    /**
     * Schedules a periodic reloading of a cache.
     * 
     * @param cache
     * @param period
     * @param unit
     */
    public static void scheduleReload(Cache<?, ?> cache, boolean isForced, long period, TimeUnit unit) {

    }

    public static void scheduleReload(CacheConfiguration<?, ?> configuration, final boolean isForced,
            final long period, final TimeUnit unit) {
        // TODO verify parameters, since we dispatch async
        configuration.runAfterStart((Procedure) new Procedure<Cache<?, ?>>() {
            public void op(Cache<?, ?> cache) {
                scheduleReload(cache, isForced, period, unit);
            }
        });

    }

    /** An Attribute indicating whether or not a loading should be forced. */
    static final class IsLoadingForcedAttribute extends BooleanAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = 1;

        /** Creates a new SizeAttribute. */
        IsLoadingForcedAttribute() {
            super("LoadingForced");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheLoadingService.IS_FORCED;
        }
    }

    static class ModuloRefreshCache implements Runnable {
        private final Cache<?, ?> cache;
        private int index;
        private final boolean isForced;
        private final Object lock = new Object();
        private final int modulo;

        public ModuloRefreshCache(Cache<?, ?> cache, boolean isForced, int modulo) {
            if (cache == null) {
                throw new NullPointerException("cache is null");
            } else if (modulo < 1) {
                throw new IllegalArgumentException("modulo must be a postive number (>0)");
            }
            this.cache = cache;
            this.modulo = modulo;
            this.isForced = isForced;
        }

        public void run() {
            final Predicate<Object> p;
            synchronized (lock) {
                if (index == modulo) {
                    index = 0;
                }
                final int fIndex = index++;
                p = new Predicate<Object>() {
                    public boolean op(Object a) {
                        return (hash(a.hashCode()) % modulo) == fIndex;
                    }
                };
            }
            if (isForced) {
                cache.filter().onKey(p).with().loadingForced().loadAll();
            } else {
                cache.filter().onKey(p).with().loading().loadAll();
            }
        }

        static int hash(int h) {
            // This function ensures that hashCodes that differ only by
            // constant multiples at each bit position have a bounded number of collisions
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }
    }

    static class ReloadCache implements Runnable {
        private final Cache<?, ?> cache;
        private final boolean isForced;

        public ReloadCache(Cache<?, ?> cache, boolean isForced) {
            if (cache == null) {
                throw new NullPointerException("cache is null");
            }
            this.cache = cache;
            this.isForced = isForced;
        }

        public void run() {
            if (isForced) {
                cache.with().loadingForced().loadAll();
            } else {
                cache.with().loading().loadAll();
            }
        }

    }

}
