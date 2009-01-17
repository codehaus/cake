/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.general;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.UnsynchronizedCache;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;

public class CacheHTTPExample {

    public static void main(String[] args) {
        CacheConfiguration<String, String> cc = CacheConfiguration.newConfiguration();
        cc.withLoading().setLoader(new UrlLoader());
        UnsynchronizedCache<String, String> c = UnsynchronizedCache.from(cc);
        readGoogle(c, "Not Cached : ");
        readGoogle(c, "Cached     : ");
    }

    public static void readGoogle(Cache<?, ?> c, String prefix) {
        long start = System.nanoTime();
        c.get("http://www.google.com");
        System.out.println(prefix + " Time to read www.google.com: " + ((System.nanoTime() - start) / 1000000.0)
                + " ms");
    }

    public static class UrlLoader implements BlockingCacheLoader<String, String> {
        public String load(String key, MutableAttributeMap ignore) throws Exception {
            URL url = new URL(key);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            int str;
            while ((str = in.read()) != -1) {
                sb.append((char) str);
            }
            in.close();
            return sb.toString();
        }
    }

}
