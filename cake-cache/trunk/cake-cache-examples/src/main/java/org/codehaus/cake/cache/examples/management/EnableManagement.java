/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

// START SNIPPET: class
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.defaults.SynchronizedCache;

public class EnableManagement {
    public static void main(String[] args) throws InterruptedException {
        CacheConfiguration<String, String> conf = CacheConfiguration
                .newConfiguration("ManagementTest");
        conf.withManagement().setEnabled(true); // enables JMX management
        Cache<String, String> cache = conf.newInstance(SynchronizedCache.class);
        cache.put("hello", "world");
        Thread.sleep(10 * 60 * 1000); // sleep 10 minutes, to allow management console to startup
    }
}
// END SNIPPET: class
