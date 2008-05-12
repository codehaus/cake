/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.general;

// START SNIPPET: class
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.UnsynchronizedCache;

public class SetNameExample {
    public static void main(String[] args) {
        CacheConfiguration<String, String> cc = CacheConfiguration.newConfiguration();
        cc.setName("MyCache");
        Cache<String, String> c = cc.newInstance(UnsynchronizedCache.class);
        System.out.println(c);
    }
}
// END SNIPPET: class

