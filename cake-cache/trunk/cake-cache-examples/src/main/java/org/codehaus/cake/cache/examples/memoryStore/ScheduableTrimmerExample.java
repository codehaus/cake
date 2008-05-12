/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.memoryStore;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.SynchronizedCache;
import org.codehaus.cake.cache.memorystore.MemoryStoreService;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ScheduableTrimmerExample.java 559 2008-01-09 16:28:27Z kasper $
 */
public class ScheduableTrimmerExample {
    static class TrimToSize implements Runnable {
        private final MemoryStoreService c;

        private final int threshold;

        private final int trimTo;

        public TrimToSize(Cache<?, ?> cache, int threshold, int trimTo) {
            if (cache == null) {
                throw new NullPointerException("cache is null");
            } else if (threshold < 0) {
                throw new IllegalArgumentException("threshold must be non negative, was "
                        + threshold);
            } else if (trimTo < 0) {
                throw new IllegalArgumentException("trimTo must be non negative, was " + trimTo);
            } else if (trimTo >= threshold) {
                throw new IllegalArgumentException("trimTo must smaller then threshold, was "
                        + trimTo + " and " + threshold);
            }
            this.threshold = threshold;
            this.trimTo = trimTo;
            c = cache.with().memoryStore();
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            c.trimToSize(trimTo);
        }
    }

    public static void main(String[] args) {
        SynchronizedCache<String, String> c = new SynchronizedCache<String, String>();
        c.with().executors().getScheduledExecutorService(null).scheduleAtFixedRate(
                new TrimToSize(c, 1100, 1000), 0, 1, TimeUnit.SECONDS);

        // other code
        c.shutdown();
    }
}
