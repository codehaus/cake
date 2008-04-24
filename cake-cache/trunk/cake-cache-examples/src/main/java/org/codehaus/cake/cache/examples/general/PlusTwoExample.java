/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.general;

// START SNIPPET: class
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.defaults.UnsynchronizedCache;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class PlusTwoExample {
    static class Plus2Loader implements SimpleCacheLoader<Integer, Integer> {
        public Integer load(Integer key, AttributeMap ignore) throws Exception {
            return key + 2;
        }
    }

    public static void main(String[] args) {
        CacheConfiguration<Integer, Integer> cc = CacheConfiguration.newConfiguration();
        cc.withLoading().setLoader(new Plus2Loader());
        UnsynchronizedCache<Integer, Integer> c = cc.newInstance(UnsynchronizedCache.class);
        System.out.println(c.get(5)); // prints 7
        System.out.println(c.get(8));// prints 10
    }
}
// END SNIPPET: class
