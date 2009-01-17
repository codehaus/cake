/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

// START SNIPPET: class
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.SynchronizedCache;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.management.ManagedObject;
@ManagedObject(defaultValue = "MyCacheLoader", description = "Cache Loading statistics")
public class CountCacheLoader implements BlockingCacheLoader<String, String> {
    /** Keeping count of the number of loads. */
    private final AtomicLong numberOfLoads = new AtomicLong();

    @ManagedAttribute(defaultValue = "numberOfLoads", description = "The number of loads by the cache loader")
    public long getNumberOfLoads() {
        return numberOfLoads.get();
    }

    public String load(String key, MutableAttributeMap attributes) {
        numberOfLoads.incrementAndGet();
        return key.toLowerCase();
    }

    public static void main(String[] args) throws InterruptedException {
        CacheConfiguration<String, String> conf = CacheConfiguration.newConfiguration("CountCacheUsage");
        conf.withLoading().setLoader(new CountCacheLoader()); // sets the loader
        conf.withManagement().setEnabled(true); // enables JMX management
        Cache<String, String> cache = SynchronizedCache.from(conf);

        // load one value each second for 10 minutes
        for (int i = 0; i < 600; i++) {
            cache.with().loading().load("count" + i);
            Thread.sleep(1000);
        }
    }
}
// END SNIPPET: class
