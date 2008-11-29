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
package org.codehaus.cake.cache.service.loading;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;

/**
 * Various Factory and utility methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServices.java 469 2007-11-17 14:32:25Z kasper $
 */
public final class CacheLoadingUtils {
    // /CLOVER:OFF
    /** Cannot instantiate. */
    private CacheLoadingUtils() {
    }

    // /CLOVER:ON

    // TODO Schedule full reload (forced / non forced)
    public void scheduleReload(Cache<?, ?> cache, int period, TimeUnit unit) {

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
    public static void scheduleForcedPartialReload(CacheConfiguration<?, ?> configuration, final int parts,
            final long period, final TimeUnit unit) {
        configuration.runAfterStart((Procedure) new Procedure<Cache<?, ?>>() {
            public void op(Cache<?, ?> cache) {
                scheduleForcedPartialReload(cache, parts, period, unit);
            }
        });
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
    public static ScheduledFuture<?> scheduleForcedPartialReload(Cache<?, ?> cache, int parts, long period,
            TimeUnit unit) {
        return scheduleForcedPartialReload(cache, cache.with().scheduledExecutor(), parts, period, unit);
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
    public static ScheduledFuture<?> scheduleForcedPartialReload(Cache<?, ?> cache, ScheduledExecutorService ses,
            int parts, long period, TimeUnit unit) {
        return ses.scheduleAtFixedRate(new ModuloRefreshCache(cache, parts), period, period, unit);
    }

    static class ModuloRefreshCache implements Runnable {
        private final Object lock = new Object();
        private final Cache<?, ?> cache;
        private final int modulo;
        private int index;

        public ModuloRefreshCache(Cache<?, ?> cache, int modulo) {
            this.cache = cache;
            this.modulo = modulo;
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
            cache.select().onKey(p).with().loadingForced().loadAll();
        }

        static int hash(int h) {
            // This function ensures that hashCodes that differ only by
            // constant multiples at each bit position have a bounded number of collisions
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }
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
}