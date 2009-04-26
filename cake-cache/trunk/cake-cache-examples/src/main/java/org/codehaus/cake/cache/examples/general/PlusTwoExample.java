/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.general;

// START SNIPPET: class
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.UnsynchronizedCache;
import org.codehaus.cake.util.ops.Ops.Op;

public class PlusTwoExample {
    static class Plus2Loader implements Op<Integer, Integer> {
        public Integer op(Integer key) {
            return key + 2;
        }
    }

    public static void main(String[] args) {
        CacheConfiguration<Integer, Integer> cc = CacheConfiguration.newConfiguration();
        cc.withLoading().setLoader(new Plus2Loader());
        UnsynchronizedCache<Integer, Integer> c = UnsynchronizedCache.from(cc);
        System.out.println(c.get(5)); // prints 7
        System.out.println(c.get(8));// prints 10
    }
}
// END SNIPPET: class
