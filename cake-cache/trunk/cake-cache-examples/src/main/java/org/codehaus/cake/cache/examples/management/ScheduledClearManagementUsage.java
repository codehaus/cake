package org.codehaus.cake.cache.examples.management;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.SynchronizedCache;

public class ScheduledClearManagementUsage {
    public static void main(String[] args) throws InterruptedException {
        CacheConfiguration<Integer, Integer> conf = CacheConfiguration.newConfiguration("CountCacheUsage");
        conf.addToLifecycle(new ScheduledClearManagement());
        conf.withManagement().setEnabled(true); // enables JMX management
        Cache<Integer, Integer> cache = SynchronizedCache.from(conf);
        
        // load one value each second for 10 minutes
        for (int i = 0; i < 6000; i++) {
            cache.put(i, i);
            Thread.sleep(100);
        }
    }
}
