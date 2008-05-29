/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

// START SNIPPET: class
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.SynchronizedCache;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.annotation.ManagedAttribute;

public class CountCacheLoader implements SimpleCacheLoader<String, String>, Manageable {
    /** Keeping count of the number of loads. */
    private final AtomicLong numberOfLoads = new AtomicLong();

    /** Add a new MBean with name=MyCacheLoader, description=Cache Loading statistics. */
    public void manage(ManagedGroup parent) {
        parent.addChild("MyCacheLoader", "Cache Loading statistics").add(this);
    }

    @ManagedAttribute(defaultValue = "numberOfLoads", description = "The number of loads by the cache loader")
    public long getNumberOfLoads() {
        return numberOfLoads.get();
    }

    public String load(String key, AttributeMap attributes) {
        numberOfLoads.incrementAndGet();
        return key.toLowerCase();
    }

    public static void main(String[] args) throws InterruptedException {
        CacheConfiguration<String, String> conf = CacheConfiguration
                .newConfiguration("CountCacheUsage");
        conf.withLoading().setLoader(new CountCacheLoader()); // sets the loader
        conf.withManagement().setEnabled(true); // enables JMX management
        Cache<String, String> cache = conf.newInstance(SynchronizedCache.class);

        // load one value each second for 10 minutes
        for (int i = 0; i < 600; i++) {
            cache.with().loading().load("count" + i);
            Thread.sleep(1000);
        }
    }
}
// END SNIPPET: class
