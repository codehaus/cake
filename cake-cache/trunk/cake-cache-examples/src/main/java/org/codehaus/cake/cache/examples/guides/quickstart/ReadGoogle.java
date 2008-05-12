/*
 * Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.guides.quickstart;

//START SNIPPET: CacheGoogle
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.UnsynchronizedCache;
import org.codehaus.cake.cache.examples.general.CacheHTTPExample.UrlLoader;

public class ReadGoogle {
    public static void main(String[] args) {
        CacheConfiguration<String, String> cc = CacheConfiguration.newConfiguration();
        cc.withLoading().setLoader(new UrlLoader());
        Cache<String, String> c = cc.newInstance(UnsynchronizedCache.class);
        readGoogle(c, "Not Cached : ");
        readGoogle(c, "Cached     : ");
    }

    public static void readGoogle(Cache<?, ?> c, String prefix) {
        long start = System.nanoTime();
        c.get("http://www.google.com");
        System.out.println(prefix + " Time to read www.google.com: "
                + ((System.nanoTime() - start) / 1000000.0) + " ms");
    }
}
// END SNIPPET: CacheGoogle
